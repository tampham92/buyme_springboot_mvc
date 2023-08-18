package com.tampham.controllers.admin;

import com.tampham.dtos.ProductDto;
import com.tampham.models.Product;
import com.tampham.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminCtr {
    @GetMapping("/admin/dashboard")
    public String getPageDashboard(Model model){
        model.addAttribute("page-name", "Dashboard");
        return "admin/dashboard";
    }
}
