package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    public Cart findByProductIdAndUserId(Integer productId, Integer userId);
}
