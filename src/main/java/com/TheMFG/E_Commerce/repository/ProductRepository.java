package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByIsActiveTrue();
    List<Product> findByCategory(String category);
}
