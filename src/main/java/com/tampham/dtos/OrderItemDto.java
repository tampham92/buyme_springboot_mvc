package com.tampham.dtos;

import com.tampham.enums.TimeLicense;
import com.tampham.models.Product;
import lombok.Getter;
import lombok.Setter;

public class OrderItemDto {

    @Getter
    @Setter
    private TimeLicense timeLicense;

    @Getter
    @Setter
    private Product product;

    public OrderItemDto(){}

    public OrderItemDto(TimeLicense timeLicense, Product product){
        this.timeLicense = timeLicense;
        this.product = product;
    }
}
