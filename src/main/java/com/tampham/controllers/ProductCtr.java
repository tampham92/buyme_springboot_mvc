package com.tampham.controllers;

import com.tampham.dtos.ProductDto;
import com.tampham.models.Product;
import com.tampham.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductCtr {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product/productList")
    public String getProducts(Model model){
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "product/product_list";
    }

    @GetMapping("/product/create")
    public String getFormProduct(Model model){
        model.addAttribute("productDto", new ProductDto());
        return "product/create_new_form";
    }

    @PostMapping("/product/create")
    public String createNewProduct(@Valid ProductDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "product/create_new_form";
        }

        Product product = new Product();
        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        productRepository.save(product);

        return "product/product_list";
    }

    @RequestMapping("/product/edit/")
    public String getFormProduct(@RequestParam("id")Long id, Model model){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            ProductDto productDto = new ProductDto(product.get());
            model.addAttribute("id", product.get().getId());
            model.addAttribute("productDto", productDto);
        }

        return "product/update_form";
    }

//    @PutMapping("")
}
