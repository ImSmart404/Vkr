package com.mtuci.vkr.controller;


import com.mtuci.vkr.model.Product;
import com.mtuci.vkr.service.WildBerriesService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("/vkr")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    final WildBerriesService wildBerriesService;
    @PostMapping()
    public ResponseEntity<?> getPage(@RequestBody String url){

        try {
            // Выполняем запрос к стороннему серверу
            URL wildBerriesUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) wildBerriesUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Считываем ответ от сервера
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Парсим JSON-данные в объект Product
                JSONObject json = new JSONObject(response.toString());
                Product product = new Product();
                product.setBrand(json.getString("brand"));
                product.setPriceU(json.getLong("priceU"));
                product.setSale(json.getLong("sale"));
                product.setRating(json.getInt("rating"));
                product.setPics(json.getInt("pics"));

                // Сохраняем объект Product в базе данных
                Product savedProduct = wildBerriesService.saveProduct(product);
                System.out.println(savedProduct);
                return ResponseEntity.ok(savedProduct);
            } else {
                return ResponseEntity.status(responseCode).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/getId")
    public ResponseEntity<HashMap<Long, String>> getIdByName(@RequestBody Map<String, String> requestMap) throws IOException, InterruptedException {
        String productName = requestMap.get("productName");
        return ResponseEntity.ok().body(wildBerriesService.getIdByName(productName)) ;
    }
    @GetMapping
    public String home(){
        return "vkr";
    }
}