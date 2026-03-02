package com.ecom.repository;

import com.ecom.entity.User;
import com.ecom.entity.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebOrderRepository extends JpaRepository<WebOrder, Long> {
    List<WebOrder> findByUser(User user);
}
