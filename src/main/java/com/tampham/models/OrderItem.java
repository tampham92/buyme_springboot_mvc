package com.tampham.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem extends BaseModel{
    @Getter
    @Setter
    @ManyToOne
    private Product product;

    @Getter
    @Setter
    private Double quantity;

    @Getter
    @Setter
    private Double total;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;
}
