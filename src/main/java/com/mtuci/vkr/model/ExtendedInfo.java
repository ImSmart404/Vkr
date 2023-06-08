package com.mtuci.vkr.model;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
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
    @OneToMany
    @JoinColumn(name = "product_id")
    private List<PriceHistory>  priceHistory = new ArrayList<>();
    private String description;
}
