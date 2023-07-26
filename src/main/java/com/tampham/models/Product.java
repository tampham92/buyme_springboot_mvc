package com.tampham.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

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
