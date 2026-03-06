package com.ecom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

@Entity
public class Cart {
    @Id
    private Long id;

    @OneToOne
    private User user;

    @OneToMany
    private List<CartItem> items;
}
