package com.tampham.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseModel {
    @Getter
    @Setter
    private String orderCode;

    @Getter
    @Setter
    private double amount;

    /**
     * Thông tin tk mua và người mua
     * */
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    private String buyerName;

    @Getter
    @Setter
    private String buyerPhone;

    @Getter
    @Setter
    private String buyerAddress;

    @Getter
    @Setter
    private String buyerEmail;
    /**
     * Thông tin thanh toán, danh sách sản phẩm và trạng thái đơn hàng
     * */

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Getter
    @Setter
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    public void calculator(){
        for (OrderItem item : getItems()){
            this.amount += item.getTotal();
        }
    }
}
