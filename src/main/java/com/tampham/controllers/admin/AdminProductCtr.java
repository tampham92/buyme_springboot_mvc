package com.tampham.controllers.admin;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AdminProductCtr {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/admin/products")
    public String getPageProduct(Model model){
        model.addAttribute("page-name", "Product");
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "admin/product";
    }

    @PostMapping("/admin/product/save")
    public String saveProduct(@Valid Product form, BindingResult bindingResult,
                              @RequestParam("fileImage1") MultipartFile image1,
                              @RequestParam("fileImage2") MultipartFile image2) throws IOException {

        if (bindingResult.hasErrors()){
            return "admin/product_form";
        }
        // Nếu có id thì cập nhật, ko thì thêm mới
        Product product;
        if (form.getId() != null){
            product = productRepository.findById(form.getId()).get();
        } else {
            product = new Product();
        }

        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        product.setDescription(form.getDescription());

        if (image1.getSize() > 0){
            product.setImage1(image1.getBytes());
        }

        if (image2.getSize() > 0){
            product.setImage2(image2.getBytes());
        }

        productRepository.save(product);

        return "redirect:/admin/products";
    }

    /**
     * Method get for create new form
     * */
    @GetMapping("/admin/product/create")
    public String getFormProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/product_form";
    }

    /**
     * Method get for update form
     * */
    @RequestMapping("/admin/product/edit/")
    public String getFormProduct(@RequestParam("id")Long id, Model model){
        if (id != null){
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);
        }

        return "admin/product_form";
    }
}
