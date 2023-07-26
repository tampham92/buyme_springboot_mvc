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

    @PostMapping("/register")
    public void createUser(RegisterDto form) {
//        Role adminRole = roleRepository.save(new Role("ADMIN"));
//        roleRepository.save(new Role("USER"));

//        Set<Role> roles = new HashSet<>();
//        roles.add(adminRole);
//
//        User user = new User("admin", passwordEncoder.encode("12345"), "Phạm Hoàng Tâm", roles);
//        usersRepository.save(user);

        User user = authService.registerUser(form);
        System.out.println(user);
    }
}
