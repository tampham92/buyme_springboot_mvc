package com.tampham.controllers.buyer;

import com.tampham.models.User;
import com.tampham.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserCtr {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/buyer/account")
    public String getAccountInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        User user = userRepository.findByUsername(currentUser).get();
        model.addAttribute("user", user);

        return "user/user_info";
    }

    @PostMapping("/buyer/updateInfo")
    public String updateInfo(@Valid User form, BindingResult bindingResult,
                             @RequestParam("imgAvatar") MultipartFile imgAvatar) throws IOException {
        if (bindingResult.hasErrors()){
            return "user/user_info";
        }

        if (form.getId() != null){
            User user = userRepository.findById(form.getId()).get();
            user.setEmail(form.getEmail());
            user.setAddress(form.getAddress());
            user.setFullName(form.getFullName());
            user.setBirthdate(form.getBirthdate());
            user.setPhoneNumber(form.getPhoneNumber());

            if (imgAvatar.getSize() > 0){
                user.setAvatar(imgAvatar.getBytes());
            }

            userRepository.save(user);
        }

        return "redirect:/buyer/account";
    }
}
