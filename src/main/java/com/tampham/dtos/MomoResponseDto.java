package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;

public class MomoResponseDto {
    @Getter
    @Setter
    private String partnerCode;

    @Getter
    @Setter
    private String requestId;

    @Getter
    @Setter
    private String orderId;

    @Getter
    @Setter
    private long amount;

    @Getter
    @Setter
    private long responseTime;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Integer resultCode;

    @Getter
    @Setter
    private String payUrl;
}
