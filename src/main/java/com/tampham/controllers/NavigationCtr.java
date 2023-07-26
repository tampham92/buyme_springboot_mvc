package com.tampham.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationCtr {
    @GetMapping("/account")
    public String getAccountInfo(Model model){
        model.addAttribute("activePage", "account");
        return "account";
    }

}
