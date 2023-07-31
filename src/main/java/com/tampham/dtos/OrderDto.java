package com.tampham.dtos;

import com.tampham.enums.PaymentType;
import com.tampham.models.OrderItem;
import com.tampham.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderDto {
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private Set<OrderItem> items;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private PaymentType paymentType = PaymentType.OTHER;

    public OrderDto(User user, Set<OrderItem> items){
        this.user = user;
        this.items = items;
    }

    public void calculator(){
        for (OrderItem item : getItems()){
            this.amount += item.getTotal();
        }
    }
}
