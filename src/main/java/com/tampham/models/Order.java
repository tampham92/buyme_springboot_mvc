package com.tampham.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tampham.enums.OrderStatus;
import com.tampham.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseModel {
    @Getter
    @Setter
    private String oderCode;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    @ManyToOne
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
    private Set<OrderItem> items = new HashSet<>();
}
