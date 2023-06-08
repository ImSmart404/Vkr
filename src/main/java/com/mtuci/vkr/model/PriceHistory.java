package com.mtuci.vkr.model;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_id")
    private Long productId;
    private Integer price;
}