package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {
    List<ProductOrder> findByUserId(Integer userId);

    ProductOrder findByOrderId(String orderId);
}
