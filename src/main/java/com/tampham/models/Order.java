package com.tampham.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

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

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

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

    public Order(){}
    public Order (User user, OrderItem item, PaymentType paymentType){
        this.user = user;
        this.getItems().add(item);
        this.paymentType = paymentType;
    }

    public void calculator(){
        for (OrderItem item : getItems()){
            this.amount += item.getTotal();
        }
    }
}
