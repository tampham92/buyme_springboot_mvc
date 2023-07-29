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

    public RegisterDto(){}

    public RegisterDto(String username, String password,
                        String fullName, String email,
                        String address, String phoneNumber){
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
