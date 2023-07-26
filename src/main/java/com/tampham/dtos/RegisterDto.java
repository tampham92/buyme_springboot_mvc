package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;

public class RegisterDto {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String phoneNumber;
}
