package com.mtuci.vkr.model;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class MainInfo {
    @Id
    private Long id;
    private String brand;
    private Long priceU;
    private Long salePrice;
    private Integer rating;
    private Integer pics;

}
