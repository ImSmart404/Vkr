package com.mtuci.vkr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ExtendedInfo {
    @Id
    private Long id;
    private String fullName;
    private String category;
    private String subCategory;
    private String options;

    private String description;
}
