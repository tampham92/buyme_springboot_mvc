package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;

public class VerifyDto {
    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private String code;
}
