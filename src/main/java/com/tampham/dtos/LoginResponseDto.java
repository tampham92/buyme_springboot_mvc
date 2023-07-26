package com.tampham.dtos;

import com.tampham.models.User;
import lombok.Getter;
import lombok.Setter;

public class LoginResponseDto {
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String token;

    public LoginResponseDto(User user, String token){
        this.user = user;
        this.token = token;
    }
}
