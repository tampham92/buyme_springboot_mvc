package com.tampham.services;

import com.tampham.models.Order;
import com.tampham.models.User;
import org.springframework.data.domain.Page;

import java.util.List;


public interface OrderService {
    Page<Order> findByUser(User user, int pageNumber, String keyword);

}
