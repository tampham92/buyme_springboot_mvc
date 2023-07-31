package com.tampham.controllers;

import com.tampham.dtos.OrderDto;
import com.tampham.dtos.OrderItemDto;
import com.tampham.enums.TimeLicense;
import com.tampham.models.OrderItem;
import com.tampham.models.Product;
import com.tampham.models.User;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class OrderCtr {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/order/checkout")
    public String checkoutOrder(OrderItemDto itemDto, Model model){
        Optional<Product> product = productRepository.findById(itemDto.getProductId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && product.isPresent()){
            String currentUser = authentication.getName();
            User user = userRepository.findByUsername(currentUser).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDto.getTimeLicense().getNumberOfYear());
            orderItem.setProduct(product.get());
            orderItem.calculator();

            Set<OrderItem> orderItems = new HashSet<>();
            orderItems.add(orderItem);

            OrderDto orderDto = new OrderDto(user, orderItems);
            orderDto.calculator();

            model.addAttribute("orderDto", orderDto);
        }

        return "order/checkout_form";
    }
}
