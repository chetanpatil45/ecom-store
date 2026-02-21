package com.ecom.service;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getProducts(){
        return repository.findAll();
    }
}
