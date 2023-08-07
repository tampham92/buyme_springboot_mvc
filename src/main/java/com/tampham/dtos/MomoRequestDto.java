package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;

public class MomoRequestDto {
    @Getter
    @Setter
    private String partnerCode;

    @Getter
    @Setter
    private String partnerName;

    @Getter
    @Setter
    private String requestId;

    @Getter
    @Setter
    private String orderId;

    @Getter
    @Setter
    private String orderInfo;

    @Getter
    @Setter
    private Long amount;

    @Getter
    @Setter
    private boolean autoCapture;

    @Getter
    @Setter
    private String redirectUrl;

    @Getter
    @Setter
    private String ipnUrl;

    @Getter
    @Setter
    private String requestType;

    @Getter
    @Setter
    private String extraData;

    @Getter
    @Setter
    private String lang;

    @Getter
    @Setter
    private String signature;
}
