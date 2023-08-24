package com.tampham.repository;

import com.tampham.models.Order;
import com.tampham.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserOrderByIdDesc(User user, Pageable pageable);

    @Query(value = "SELECT o.* FROM orders o JOIN users u ON o.user_id = u.id WHERE u.id = ?1 AND o.order_code LIKE %?2% ORDER BY o.id desc",
            countQuery = "SELECT count(*) FROM orders o JOIN users u ON o.user_id = u.id WHERE u.id = ?1 AND o.order_code LIKE %?2%",
            nativeQuery = true)
    Page<Order> findAll(long userId, String keyword, Pageable pageable);

    Order findByOrderCode(String orderCode);
}
