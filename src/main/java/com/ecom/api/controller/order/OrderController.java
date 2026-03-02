package com.ecom.api.controller.order;

import com.ecom.entity.User;
import com.ecom.entity.WebOrder;
import com.ecom.service.WebOrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final WebOrderService orderService;

    public OrderController(WebOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal User user){
        return orderService.getOrders(user);
    }
}
