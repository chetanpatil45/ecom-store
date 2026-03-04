package com.ecom.repository;

import com.ecom.entity.User;
import com.ecom.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
