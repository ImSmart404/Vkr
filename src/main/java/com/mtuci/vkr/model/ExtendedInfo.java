package com.mtuci.vkr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ExtendedInfo {
    @Id
    private Long id;
    private String fullName;
    private String category;
    private String subCategory;
    private String options;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private List<PriceHistory>  priceHistory;
    private String description;
}
