package com.tampham.repository;

import com.tampham.models.Order;
import com.tampham.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserOrderByIdDesc(User user, Pageable pageable);

//    @Query("select o from orders o join o.user u where u.id=?1 and o.oder_code like %?2%")
//    Page<Order> findAll(long userId, String keyword, Pageable pageable);

    Order findByOrderCode(String orderCode);
}
