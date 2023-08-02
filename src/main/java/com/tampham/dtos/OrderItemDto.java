package com.tampham.dtos;

import com.tampham.enums.TimeLicense;
import com.tampham.models.Product;
import lombok.Getter;
import lombok.Setter;

public class OrderItemDto {

    @Getter
    @Setter
    private Double quantity;

    @Getter
    @Setter
    private Product product;

    public OrderItemDto(){}

    public OrderItemDto(Double quantity, Product product){
        this.quantity = quantity;
        this.product = product;
    }
}
