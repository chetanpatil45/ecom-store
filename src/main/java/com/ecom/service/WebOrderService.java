package com.ecom.service;

import com.ecom.entity.User;
import com.ecom.entity.WebOrder;
import com.ecom.repository.WebOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebOrderService {
    private final WebOrderRepository repository;

    public WebOrderService(WebOrderRepository repository) {
        this.repository = repository;
    }

    public List<WebOrder> getOrders(User user){
        return repository.findByUser(user);
    }
}
