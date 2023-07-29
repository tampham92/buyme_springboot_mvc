package com.tampham.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
public class Product extends BaseModel{
    @Getter
    @Setter
    private String productName;

    @Getter
    @Setter
    private Double price;
}
