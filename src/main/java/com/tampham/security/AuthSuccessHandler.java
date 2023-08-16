package com.tampham.security;

import com.tampham.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User)authentication.getPrincipal();
        String redirectUrl = request.getContextPath();

        if (user.hasRole("ADMIN")){
            redirectUrl = "/admin/dashboard";
        } else if (user.hasRole("USER")){
            redirectUrl = "/buyer/account";
        }
        response.sendRedirect(redirectUrl);
    }
}
