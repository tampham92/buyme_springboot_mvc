package com.tampham.controllers;

import com.tampham.dtos.RegisterDto;
import com.tampham.models.Role;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthCtr {

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository usersRepository;

    @GetMapping("/register")
    public String registerUser() {
        return "auth/register";
    }

    @PostMapping("/register")
    public void createUser(RegisterDto form) {
        System.out.println(form.getUsername());
        User user = authService.registerUser(form);
    }
}
