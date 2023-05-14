package com.mtuci.vkr.controller;


import com.mtuci.vkr.model.ExtendedInfo;
import com.mtuci.vkr.model.MainInfo;
import com.mtuci.vkr.service.WildBerriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/vkr")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    final WildBerriesService wildBerriesService;

    @GetMapping("/getProductsMainInfo")
    public ResponseEntity<List<MainInfo>> getProductsMainInfo(@RequestParam("productName") String productName, @RequestParam(value = "pages") Integer pages) throws InterruptedException, IOException {
        return ResponseEntity.ok().body(wildBerriesService.getProductsInfo(productName, pages)) ;
    }
    @GetMapping("/getProductExtendedInfo")
    public ResponseEntity<Optional<ExtendedInfo>> getProductsExtendedInfo(@RequestParam("id") Long id){
        return ResponseEntity.ok().body(wildBerriesService.getProductExtendedInfo(id));
    }
    @GetMapping
    public String home(){
        return "vkr";
    }
}