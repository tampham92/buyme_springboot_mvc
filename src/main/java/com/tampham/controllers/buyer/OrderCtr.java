package com.tampham.controllers.buyer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tampham.dtos.MomoResponseDto;
import com.tampham.dtos.OrderItemDto;
import com.tampham.dtos.UpdatePaymentTypeDto;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import com.tampham.models.Order;
import com.tampham.models.OrderItem;
import com.tampham.models.User;
import com.tampham.repository.OrderRepository;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.UserRepository;
import com.tampham.services.HashIdService;
import com.tampham.services.PaymentService;
import com.tampham.utils.RandomStringUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private HashIdService hashIdService;

    @Getter
    private List<PaymentType> paymentTypes = List.of(PaymentType.values());

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

//        if (ROLE.equals("ADMIN")){
//            orders = orderRepository.findAll();
//        }

        model.addAttribute("hashIdService", hashIdService);
        model.addAttribute("orders", orders);
        return "order/order_list";
    }

    @GetMapping("/order/detail/")
    public String getOrderDetail(@RequestParam("id") String orderId, Model model){
        if (orderId.isEmpty()){
            return "order/order_list";
        }

        long id = hashIdService.deCodeId(orderId);
        Order order = orderRepository.findById(id).get();

        model.addAttribute("paymentTypes", paymentTypes);
        model.addAttribute("updatePayment", new UpdatePaymentTypeDto(hashIdService.endCodeId(order.getId()), order.getPaymentType()));
        model.addAttribute("orderInfo", order);
        model.addAttribute("hashIdService", hashIdService);
        return "order/order_detail";
    }

    /**
     * Update lại phương thức thanh toán nếu đơn hàng chưa thanh toán đơn và muốn đổi phương thức thanh toán khác.
     * Sau khi update paymentType thì redirect lại đúng thông tin order cho client để tiến hành thanh toán
     * @param: paymentType
     * @param: orderId
     * */
    @PostMapping("/order/updatePaymentType")
    public String updatePaymentType(UpdatePaymentTypeDto form, RedirectAttributes attributes){
        long orderId = hashIdService.deCodeId(form.getOrderId());
        Order order = orderRepository.findById(orderId).get();
        order.setPaymentType(form.getPaymentType());
        orderRepository.save(order);

        attributes.addAttribute("id", form.getOrderId());
        return "redirect:/order/detail/";
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

            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("order", order);
        }

        return "order/checkout_form";
    }

    /**
    * Route này thực hiện kiểm tra phương thức thanh toán, và lưu đơn hàng
    * @param: orderForm
    * */
    @PostMapping("/order/payment")
    public String doPayment(Order form, Model model, RedirectAttributes attributes) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Map<String, String> errors = validate(form);
        if (!errors.isEmpty()){
            model.addAttribute("paymentTypes", paymentTypes);
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
        order.setOrderCode("TM-" + RandomStringUtils.getAlphaNumericString(5).toUpperCase());
        order.calculator(); // Tính tổng lại trước khi lưu đơn

        // Nếu là phương thức thanh toán OTHER thì lưu đơn hàng và trả về success
        if (form.getPaymentType().equals(PaymentType.OTHER)){
            order.setPaymentType(form.getPaymentType());
            order.setStatus(OrderStatus.SUCCESS);
            orderRepository.save(order);

            attributes.addAttribute("orderId", hashIdService.endCodeId(order.getId()));
            return "redirect:/order/orderNotice";
        }

        // Nếu là phương thức thanh toán MOMO thì gọi service thanh toán momo
        if (form.getPaymentType().equals(PaymentType.MOMO)){
            order.setPaymentType(form.getPaymentType());
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            String orderInfo = "Mua goi " + order.getItems().get(0).getProduct().getProductName();
            MomoResponseDto response = (MomoResponseDto) paymentService.createPayment(Double.valueOf(order.getAmount()).longValue(), order.getOrderCode(), orderInfo);
            if (response.getPayUrl() == null){
                String message = "Có lỗi xảy ra vui lòng chọn phương thức thanh toán khác";
                model.addAttribute("errorMsg", message);
                model.addAttribute("paymentTypes", paymentTypes);
                return "order/checkout_form";
            }
            return "redirect:" + response.getPayUrl();
        }

        return "redirect:/order/orderList";
    }

    @GetMapping("/order/rePayment/")
    public String updateOrder(@RequestParam("id") String orderId, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (orderId.isEmpty()){
            return "redirect:/order/orderList";
        }

        long id = hashIdService.deCodeId(orderId);
        Order order = orderRepository.findById(id).get();
        order.setOrderCode("TM-" + RandomStringUtils.getAlphaNumericString(5).toUpperCase());

        // Cập nhật lại đơn hàng
        if (order.getPaymentType().equals(PaymentType.MOMO)){
            orderRepository.save(order);

            String orderInfo = "Mua goi " + order.getItems().get(0).getProduct().getProductName();
            MomoResponseDto response = (MomoResponseDto) paymentService.createPayment(Double.valueOf(order.getAmount()).longValue(), order.getOrderCode(), orderInfo);
            return "redirect:" + response.getPayUrl();
        } else{
            order.setStatus(OrderStatus.SUCCESS);
            orderRepository.save(order);

            redirectAttributes.addAttribute("orderId", hashIdService.endCodeId(order.getId()));
            return "redirect:/order/orderNotice";
        }
    }

    /**
     * route này hứng response sau khi thanh toán từ momo post qua ipn để cập nhật lại Order
     * @param:
     * */
    @PostMapping("/order/resultPayment/momo")
    public ResponseEntity<?> getResultPaymentMomo(@RequestBody MomoResponseDto momoResponse){
        System.out.println("Post - " + momoResponse.getMessage());
        return ResponseEntity.status(200).body("OK");
    }

    /**
    * route này hứng response sau khi thanh toán từ momo trả về để cập nhật lại Order
    * @param:
    * */
    @GetMapping("/order/resultPayment/momo")
    public String getResultPaymentMomo(@RequestParam("partnerCode")String partnerCode,
                                       @RequestParam("orderId")String orderId,
                                       @RequestParam("requestId")String requestId,
                                       @RequestParam("amount")Long amount,
                                       @RequestParam("orderInfo")String orderInfo,
                                       @RequestParam("transId")Long transId,
                                       @RequestParam("resultCode")int resultCode,
                                       @RequestParam("message")String message,
                                       @RequestParam("payType")String payType,
                                       @RequestParam("responseTime")Long responseTime,
                                       @RequestParam("extraData")String extraData,
                                       @RequestParam("signature")String signature, RedirectAttributes attributes){


        Order order = orderRepository.findByOrderCode(orderId);
        System.out.println(message);
        System.out.println(resultCode);
        if (resultCode == 0){
            order.setStatus(OrderStatus.SUCCESS);
            orderRepository.save(order);
        }
        attributes.addAttribute("orderId", hashIdService.endCodeId(order.getId()));
        return "redirect:/order/orderNotice";
    }

    @GetMapping("/order/orderNotice")
    public String orderNotice(@RequestParam("orderId") String orderId, Model model){
        long id = hashIdService.deCodeId(orderId);
        Order order = orderRepository.findById(id).get();
        model.addAttribute("orderInfo", order);
        return "order/order_notice";
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
