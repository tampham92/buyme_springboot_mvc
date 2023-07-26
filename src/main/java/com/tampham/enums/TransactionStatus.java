package com.tampham.enums;

import lombok.Getter;

public enum TransactionStatus {
    SUCCEED("Hoàn Tất"),
    FAILED("Lỗi");

    @Getter
    private String value;

    TransactionStatus(String value){
        this.value = value;
    }

}
