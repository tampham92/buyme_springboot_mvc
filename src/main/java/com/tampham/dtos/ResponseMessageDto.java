package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;

public class ResponseMessageDto {
    @Getter
    @Setter
    private boolean status;

    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private String message;

    public ResponseMessageDto(boolean status, Object data, String message){
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
