package com.ecom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class CartItem {
    @Id
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;
}
