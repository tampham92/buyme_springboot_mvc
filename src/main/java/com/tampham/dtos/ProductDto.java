package com.tampham.dtos;

import com.tampham.models.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class ProductDto {
    @Getter
    @Setter
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Getter
    @Setter
    private Double price;

    public ProductDto(){}

    public ProductDto(Product product){
        this.productName = product.getProductName();
        this.price = product.getPrice();
    }
}
