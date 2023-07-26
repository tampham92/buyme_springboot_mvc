package com.tampham.controllers;

import com.tampham.dtos.ProductDto;
import com.tampham.models.Product;
import com.tampham.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductCtr {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String getProducts(Model model){

        List<Product> products = productRepository.findAll();

        model.addAttribute("activePage", "product");
        model.addAttribute("products", products);

        return "product";
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createNewProduct(@RequestBody ProductDto body){
        Product product = new Product();
        product.setProductName(body.getProductName());
        product.setPrice(body.getPrice());
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
