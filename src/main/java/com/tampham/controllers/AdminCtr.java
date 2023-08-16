package com.tampham.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCtr {
    @GetMapping("/admin/dashboard")
    public String getPageDashboard(Model model){
        model.addAttribute("page-name", "Dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/admin/product")
    public String getPageProduct(Model model){
        model.addAttribute("page-name", "Product");
        return "admin/product";
    }
}
