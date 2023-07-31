package com.tampham.dtos;

import com.tampham.models.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class ProductDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Getter
    @Setter
    private Double price;

    @Getter
    @Setter
    private String description;

    public ProductDto(){}

    public ProductDto(Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }
}
