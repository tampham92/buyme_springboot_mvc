package com.tampham.services;

import com.tampham.models.Order;
import com.tampham.models.User;
import com.tampham.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<Order> findByUser(User user, int pageNumber, String keyword) {
        if (user != null){
            Pageable pageable = PageRequest.of(pageNumber -1, 2);
            if (keyword != null){
                return orderRepository.findAll(user.getId(), keyword, pageable);
            }
            return orderRepository.findByUserOrderByIdDesc(user, pageable);
        }
        return null;
    }
}
