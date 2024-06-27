package com.TheMFG.E_Commerce.service.Impl;

import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.repository.ProductRepository;
import com.TheMFG.E_Commerce.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
