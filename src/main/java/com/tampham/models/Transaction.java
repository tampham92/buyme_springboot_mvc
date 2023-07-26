package com.tampham.models;

import com.tampham.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction extends BaseModel{
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    @ManyToOne
    private Order order;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String resultCode;
}
