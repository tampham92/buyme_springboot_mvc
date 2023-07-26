package com.tampham.enums;

import lombok.Getter;

public enum PaymentType {
    MOMO("Momo"),
    OTHER("Khác");

    @Getter
    private String value;

    PaymentType (String value){
        this.value = value;
    }
}
