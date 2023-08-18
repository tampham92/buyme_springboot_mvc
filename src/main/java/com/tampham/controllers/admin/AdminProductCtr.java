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

    @GetMapping("/admin/productNew")
    public String productNew(){
        return "admin/product_new";
    }

    @PostMapping("/admin/product/save")
    public String createNewProduct(@Valid ProductDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "admin/product_form";
        }

        Product product;
        if (form.getId() != null){
            product = productRepository.findById(form.getId()).get();
        } else {
            product = new Product();
        }

        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        product.setDescription(form.getDescription());
        productRepository.save(product);

        return "redirect:/admin/products";
    }

    /**
     * Method get for create new form
     * */
    @GetMapping("/admin/product/create")
    public String getFormProduct(Model model){
        model.addAttribute("productDto", new ProductDto());
        return "admin/product_form";
    }

    /**
     * Method get for update form
     * */
    @RequestMapping("/admin/product/edit/")
    public String getFormProduct(@RequestParam("id")Long id, Model model){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            ProductDto productDto = new ProductDto(product.get());
            model.addAttribute("productDto", productDto);
        }

        return "admin/product_form";
    }
}
