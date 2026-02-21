package com.ecom.api.controller.order;

import com.ecom.model.LocalUser;
import com.ecom.model.WebOrder;
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

    public OrderController(WebOrderService orderServicel) {
        this.orderService = orderServicel;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user){
        return orderService.getOrders(user);
    }
}
