package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;


public class LoginDto {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;
}
