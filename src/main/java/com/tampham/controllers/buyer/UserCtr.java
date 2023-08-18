package com.tampham.controllers.buyer;

import com.tampham.models.User;
import com.tampham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class UserCtr {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/buyer/account")
    public String getAccountInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        User user = userRepository.findByUsername(currentUser).get();
        model.addAttribute("activePage", "account");
        model.addAttribute("user", user);

        return "user/user_info";
    }


}
