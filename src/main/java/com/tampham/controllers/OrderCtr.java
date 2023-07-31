package com.tampham.controllers;

import com.tampham.dtos.OrderItemDto;
import com.tampham.enums.PaymentType;
import com.tampham.models.Order;
import com.tampham.models.OrderItem;
import com.tampham.models.User;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class OrderCtr {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/order/checkout")
    public String checkoutOrder(OrderItemDto itemDto, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && itemDto.getProduct() != null){
            String currentUser = authentication.getName();
            User user = userRepository.findByUsername(currentUser).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDto.getTimeLicense().getNumberOfYear());
            orderItem.setProduct(itemDto.getProduct());
            orderItem.calculator(); // tính toán để lấy giá trị item total

            Set<OrderItem> orderItems = new HashSet<>();
            orderItems.add(orderItem);

            Order order = new Order();
            order.setUser(user);
            order.setItems(orderItems);
            order.calculator(); // tính toán để lấy giá trị amount

            List<PaymentType> paymentTypes = List.of(PaymentType.values());

            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("order", order);
        }

        return "order/checkout_form";
    }


}
