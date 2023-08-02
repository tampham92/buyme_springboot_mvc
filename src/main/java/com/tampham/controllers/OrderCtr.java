package com.tampham.controllers;

import com.tampham.dtos.OrderItemDto;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import com.tampham.models.Order;
import com.tampham.models.OrderItem;
import com.tampham.models.User;
import com.tampham.repository.OrderRepository;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.UserRepository;
import com.tampham.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class OrderCtr {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order/orderList")
    public String getAllOrder(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra user phải đăng nhập, và check ROLE nếu là ADMIN thì ko cần filter
        String ROLE = null;
        if (authentication != null){
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()){
                ROLE = grantedAuthority.getAuthority();
            }
        }

        List<Order> orders = new ArrayList<>();
        if (ROLE.equals("USER")){
            User user = userRepository.findByUsername(authentication.getName()).get();
            orders = orderRepository.findByUser(user);
        }

        if (ROLE.equals("ADMIN")){
            orders = orderRepository.findAll();
        }

        model.addAttribute("orders", orders);
        return "order/order_list";
    }

    @PostMapping("/order/checkout")
    public String checkoutOrder(OrderItemDto itemDto, Model model){
        // Kiểm tra xem user đã đăng nhập chưa, mới được vào phần checkout
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && itemDto.getProduct() != null){
            String currentUser = authentication.getName();
            User user = userRepository.findByUsername(currentUser).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setProduct(itemDto.getProduct());
            orderItem.calculator(); // tính toán để lấy giá trị item total

            Order order = new Order(user, orderItem, PaymentType.OTHER);
            order.calculator(); // tính toán để lấy giá trị amount

            List<PaymentType> paymentTypes = List.of(PaymentType.values());

            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("order", order);
        }

        return "order/checkout_form";
    }

    @PostMapping("/order/payment")
    public String doPayment(Order form, Model model){
        Map<String, String> errors = validate(form);
        Map<String, String> successMsg = new HashMap<>();

        if (!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "order/checkout_form";
        }

        User user = userRepository.findById(form.getUser().getId()).get();
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        //Cap nhat item
        for (OrderItem item : form.getItems()){
            item.setOrder(order);
            items.add(item);
        }

        // Nếu là phương thức thanh toán OTHER thì trả về success
        if (form.getPaymentType().equals(PaymentType.OTHER)){
            order.setStatus(OrderStatus.SUCCESS);
        }

        // Nếu là phương thức thanh toán MOMO thì gọi api thanh toán momo
        if (form.getPaymentType().equals(PaymentType.MOMO)){
            order.setStatus(OrderStatus.PENDING);
        }


        order.setPaymentType(form.getPaymentType());
        order.setItems(items);
        order.setUser(user);
        order.setOrderCode(RandomString.getAlphaNumericString(5));
        order.calculator();

        orderRepository.save(order);
        return "redirect:/order/orderList";
    }

    private Map<String, String> validate(Order form){
        Map<String, String> error = new HashMap<>();

        if (form.getUser().getFullName().isEmpty()){
            error.put("fullName", "Vui lòng nhập tên của bạn");
        }
        if (form.getUser().getPhoneNumber().isEmpty()){
            error.put("phoneNumber", "Vui lòng nhập số điện thoại của bạn");
        }
        if (form.getUser().getEmail().isEmpty()){
            error.put("email", "Vui lòng nhập email của bạn");
        }

        return error;
    }
}
