package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {
}
