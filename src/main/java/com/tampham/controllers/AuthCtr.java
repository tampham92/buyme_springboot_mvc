package com.tampham.controllers;

import com.tampham.dtos.LoginDto;
import com.tampham.dtos.RegisterDto;
import com.tampham.models.Role;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

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

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @GetMapping("/buyer/register")
    public String registerUser(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/buyer/register")
    public String createUser(@Valid RegisterDto form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "auth/register";
        }

        authService.registerUser(form);
        return "auth/login";

    }

    /**
     * Login controller
     * */
    @GetMapping("/buyer/login")
    public String loginMe() {
        return "auth/login";
    }

//    @PostMapping("/buyer/login")
//    public String loginMe(LoginDto form, Model model) {
//        System.out.println(form.getUsername());
//        Map<String, String> errors = validateLogin(form);
//        System.out.println(errors.get("username"));
//        if (!errors.isEmpty()){
//            model.addAttribute("errors", errors);
//            return "auth/login";
//        }
//        return "buyer/account";
//    }


    private Map<String, String> validateLogin(LoginDto form){
        Map<String, String> error = new HashMap<>();

        if (form.getUsername().isEmpty()){
            error.put("username", "Tên đăng nhập không được bỏ trống");
        }

        if (form.getPassword().isEmpty()){
            error.put("password", "Mật khẩu không được bỏ trống");
        }
        if (!form.getPassword().isEmpty() && !form.getUsername().isEmpty()) {
            Optional<User> user = usersRepository.findByUsername(form.getUsername());
            if (user.isEmpty()) {
                error.put("username", "Tên đăng nhập không đúng");
            }

            if (user.isPresent()){
                boolean isCheckPassword = passwordEncoder.matches(form.getPassword(), user.get().getPassword());
                if (!isCheckPassword){
                    error.put("password", "Mật khẩu không đúng");
                }
            }

        }

        return error;
    }

//    @PostMapping("/buyer/logout")
//    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//        this.logoutHandler.logout(request, response, authentication);
//        return "redirect:/";
//    }
}
