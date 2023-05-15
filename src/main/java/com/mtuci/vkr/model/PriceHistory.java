package com.mtuci.vkr.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long productId;

    private Integer price;
}