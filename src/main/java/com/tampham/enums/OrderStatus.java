package com.tampham.enums;

import lombok.Getter;

public enum OrderStatus {
    SUCCESS("Đã mua hàng"),
    CANClED("Đã huỷ"),
    PENDING("Chờ thanh toán");

    @Getter
    private String value;

    OrderStatus(String value){
        this.value = value;
    }
}
