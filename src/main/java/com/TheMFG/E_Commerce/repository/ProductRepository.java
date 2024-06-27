package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
