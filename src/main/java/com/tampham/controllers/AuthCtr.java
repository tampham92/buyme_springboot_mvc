package com.tampham.controllers;

import com.tampham.dtos.LoginDto;
import com.tampham.dtos.RegisterDto;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/auth/register")
    public String registerUser(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String createUser(RegisterDto form, Model model) {
        Map<String, String> errors = validateRegister(form);
        if (!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "auth/register";
        }

        authService.registerUser(form);
        return "auth/login";
    }

    /**
     * Login custom for login spring security
     * */
    @GetMapping("/auth/login")
    public String loginMe() {
        return "auth/login";
    }

    @GetMapping("/auth/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        return "redirect:/product/productList";
    }

    private Map<String, String> validateRegister(RegisterDto form){
        Map<String, String> error = new HashMap<>();

        if (form.getUsername().isEmpty()){
            error.put("username", "Tên đăng nhập không được bỏ trống");
        }

        if (form.getPassword().isEmpty()){
            error.put("password", "Mật khẩu không được bỏ trống");
        }

        if (!form.getUsername().isEmpty()) {
            Optional<User> user = usersRepository.findByUsername(form.getUsername());
            if (user.isPresent()) {
                error.put("username", "Tên đăng nhập đã tồn tại");
            }
        }

        return error;
    }
}
