package com.mtuci.vkr.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand;
    private Long priceU;
    private Long sale;
    private Integer rating;

    private Integer pics;

}
