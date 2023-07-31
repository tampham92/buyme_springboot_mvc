package com.tampham.controllers;

import com.tampham.dtos.OrderItemDto;
import com.tampham.dtos.ProductDto;
import com.tampham.enums.TimeLicense;
import com.tampham.models.Product;
import com.tampham.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/product/save")
    public String createNewProduct(@Valid ProductDto form, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "product/product_form";
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

        return "redirect:/product/productList";
    }

    /**
     * Method get for create new form
     * */
    @GetMapping("/product/create")
    public String getFormProduct(Model model){
        model.addAttribute("productDto", new ProductDto());
        return "product/product_form";
    }

    /**
     * Method get for update form
     * */
    @RequestMapping("/product/edit/")
    public String getFormProduct(@RequestParam("id")Long id, Model model){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            ProductDto productDto = new ProductDto(product.get());
            model.addAttribute("productDto", productDto);
        }

        return "product/product_form";
    }

    @RequestMapping("/product/detail/")
    public String getDetailProduct(@RequestParam("id")Long id, Model model){
        if (id != null){
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);
            List<TimeLicense> timeLicenses = List.of(TimeLicense.values());
            model.addAttribute("timeLicenses", timeLicenses);

            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(product.getId());

            model.addAttribute("orderItemDto",orderItemDto);
            return "product/product_detail";
        }

        return "product/product_list";
    }
}
