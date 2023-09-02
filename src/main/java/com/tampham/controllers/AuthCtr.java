package com.tampham.controllers;

import com.tampham.dtos.RegisterDto;
import com.tampham.dtos.VerifyDto;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.AuthService;
import com.tampham.services.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

    @Autowired
    private EmailService emailService;

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

        // Tạo user mới và thực hiện gửi code đến email
        User user = authService.registerUser(form);
        boolean isSendMail = emailService.sendVerifyCode(user);

        if (isSendMail){
            VerifyDto verifyDto = new VerifyDto();
            verifyDto.setUserId(user.getId());
            model.addAttribute("verifyDto", verifyDto);
        }

        return "auth/verify";
    }

    /**
     * Route này sau khi đăng ký thành công sẽ post verifycCode và userId để check verify
     * @param verifyDto
     * @param model
     * @return
     */
    @PostMapping("/auth/verify")
    public String verifyUser(VerifyDto verifyDto, Model model){
        String code = StringUtils.trimAllWhitespace(verifyDto.getCode());
        User user = usersRepository.findByIdAndVerifyCode(verifyDto.getUserId(), code);

        if (user != null){
            user.setEnable(true);
            usersRepository.save(user);
            return "redirect:/auth/login";
        } else {
            model.addAttribute("errorMsg", "Xác thực không thành công");
            return "auth/verify";
        }
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

        if (form.getEmail().isEmpty()){
            error.put("email", "Email không được bỏ trống");
        }

        if (!form.getEmail().isEmpty() && !EmailValidator.getInstance().isValid(form.getEmail())){
            error.put("email", "Email không đúng định dạng");
        }

        return error;
    }
}
