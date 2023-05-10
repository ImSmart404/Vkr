package com.mtuci.vkr.controller;


import com.mtuci.vkr.service.WildBerriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("/vkr")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    final WildBerriesService wildBerriesService;

    @GetMapping("/getId")
    public ResponseEntity<HashMap<Long, String>> getIdByName(@RequestBody Map<String, String> requestMap) throws InterruptedException {
        String request = requestMap.get("productName");
        Integer numOfPages = Integer.valueOf(requestMap.get("pages"));
        return ResponseEntity.ok().body(wildBerriesService.getIdByName(request, numOfPages)) ;
    }
    @GetMapping
    public String home(){
        return "vkr";
    }
}