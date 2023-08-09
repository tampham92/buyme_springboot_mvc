package com.tampham.repository;

import com.tampham.models.Order;
import com.tampham.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Order findByOrderCode(String orderCode);
}
