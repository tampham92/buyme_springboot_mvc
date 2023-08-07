package com.tampham.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tampham.dtos.MomoResponseDto;
import com.tampham.dtos.OrderItemDto;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import com.tampham.models.Order;
import com.tampham.models.OrderItem;
import com.tampham.models.User;
import com.tampham.repository.OrderRepository;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.PaymentService;
import com.tampham.utils.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
public class OrderCtr {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;

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
        // Kiểm tra xem user đã đăng nhập chưa, nếu đã đăng nhập mới được vào phần checkout
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && itemDto.getProduct() != null){
            String currentUser = authentication.getName();
            User user = userRepository.findByUsername(currentUser).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setProduct(itemDto.getProduct());
            orderItem.calculator(); // tính toán để lấy giá trị item total

            // Phần này chỉ khởi tạo gửi lên form chưa lưu DB
            Order order = new Order();
            order.setBuyerName(user.getFullName());
            order.setBuyerAddress(user.getAddress());
            order.setBuyerPhone(user.getPhoneNumber());
            order.setBuyerEmail(user.getEmail());
            order.setUser(user);
            order.getItems().add(orderItem);
            order.setPaymentType(PaymentType.OTHER);
            order.calculator(); // tính toán để lấy giá trị amount hiển thị lên form

            List<PaymentType> paymentTypes = List.of(PaymentType.values());

            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("order", order);
        }

        return "order/checkout_form";
    }

    @PostMapping("/order/payment")
    public String doPayment(Order form, Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Map<String, String> errors = validate(form);
        String successMsg;

        if (!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "order/checkout_form";
        }

        User user = userRepository.findById(form.getUser().getId()).get();

        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();

        // Cập nhật order Id cho item
        for (OrderItem item : form.getItems()){
            item.setOrder(order);
            items.add(item);
        }

        // Cập nhật thông tin người mua hàng và tài khoản của người mua
        order.setBuyerName(form.getBuyerName());
        order.setBuyerAddress(form.getBuyerAddress());
        order.setBuyerPhone(form.getBuyerPhone());
        order.setBuyerEmail(form.getBuyerEmail());
        order.setUser(user);

        order.setItems(items);
        order.setOrderCode(RandomStringUtils.getAlphaNumericString(5));
        order.calculator(); // Tính tổng lại trước khi lưu đơn

        // Nếu là phương thức thanh toán OTHER thì lưu đơn hàng và trả về success
        if (form.getPaymentType().equals(PaymentType.OTHER)){
            order.setPaymentType(form.getPaymentType());
            order.setStatus(OrderStatus.SUCCESS);
            orderRepository.save(order);

            successMsg = "Đã đặt hàng";
            model.addAttribute("successMsg", successMsg);

            return "order/success_order_form";
        }

        // Nếu là phương thức thanh toán MOMO thì gọi api thanh toán momo
        if (form.getPaymentType().equals(PaymentType.MOMO)){
            order.setPaymentType(form.getPaymentType());
            order.setStatus(OrderStatus.PENDING);
            String orderInfo = "Mua goi " + order.getItems().get(0).getProduct().getProductName();
            MomoResponseDto response = (MomoResponseDto) paymentService.createPayment(Double.valueOf(order.getAmount()).longValue(), "TM-" + order.getOrderCode().toUpperCase(), orderInfo);
            return "redirect:" + response.getPayUrl();
        }

        orderRepository.save(order);
        return "redirect:/order/orderList";
    }

    private Map<String, String> validate(Order form){
        Map<String, String> error = new HashMap<>();

        if (form.getBuyerName().isEmpty()){
            error.put("fullName", "Vui lòng nhập tên của bạn");
        }
        if (form.getBuyerPhone().isEmpty()){
            error.put("phoneNumber", "Vui lòng nhập số điện thoại của bạn");
        }
        if (form.getBuyerEmail().isEmpty()){
            error.put("email", "Vui lòng nhập email của bạn");
        }

        return error;
    }
}
