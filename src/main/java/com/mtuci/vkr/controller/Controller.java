package com.mtuci.vkr.controller;


import com.mtuci.vkr.model.Product;
import com.mtuci.vkr.service.WildBerriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("/vkr")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    final WildBerriesService wildBerriesService;

    @GetMapping("/getProducts")
    public ResponseEntity<List<Product>> getIdByName(@RequestParam("productName") String productName, @RequestParam(value = "pages", defaultValue = "50") Integer pages) throws InterruptedException, IOException {
        return ResponseEntity.ok().body(wildBerriesService.getProductsInfo(productName, pages)) ;
    }
    @GetMapping
    public String home(){
        return "vkr";
    }
}