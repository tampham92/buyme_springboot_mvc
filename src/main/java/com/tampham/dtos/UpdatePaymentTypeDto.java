package com.tampham.dtos;

import com.tampham.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

public class UpdatePaymentTypeDto {
    @Getter
    @Setter
    private Long orderId;

    @Getter
    @Setter
    private PaymentType paymentType;

    public UpdatePaymentTypeDto(){}

    public UpdatePaymentTypeDto(Long orderId, PaymentType paymentType) {
        this.orderId = orderId;
        this.paymentType = paymentType;
    }
}
