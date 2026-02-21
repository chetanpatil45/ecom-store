package com.ecom.repository;

import com.ecom.model.LocalUser;
import com.ecom.model.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebOrderRepository extends JpaRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);
}
