package com.tampham.dtos;

import com.tampham.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

public class UpdatePaymentTypeDto {
    @Getter
    @Setter
    private String orderId;

    @Getter
    @Setter
    private PaymentType paymentType;

    public UpdatePaymentTypeDto(){}

    public UpdatePaymentTypeDto(String orderId, PaymentType paymentType) {
        this.orderId = orderId;
        this.paymentType = paymentType;
    }
}
