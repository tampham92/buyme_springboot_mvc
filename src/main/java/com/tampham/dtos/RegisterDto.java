package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegisterDto {
    @Getter
    @Setter
    @NotEmpty(message = "Username không được để trống")
    private String username;

    @Getter
    @Setter
    @NotEmpty(message = "Password không được để trống")
    private String password;

    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    @Length(min = 10, max = 11, message = "Số điện thoại không đúng định dạng")
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
