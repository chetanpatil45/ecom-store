package com.ecom.service;

import com.ecom.entity.Cart;
import com.ecom.repository.CartItemRepository;
import com.ecom.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart addProductToCart(){
        return null;
    }

    public void removeProductFromCart(){

    }

    public Cart getCart(){
        return null;
    }

    public Cart updateQuantity(){
        return null;
    }
}
