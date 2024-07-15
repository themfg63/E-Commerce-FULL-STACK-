package com.TheMFG.E_Commerce.service.Impl;

import com.TheMFG.E_Commerce.model.Cart;
import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.model.User;
import com.TheMFG.E_Commerce.repository.CartRepository;
import com.TheMFG.E_Commerce.repository.ProductRepository;
import com.TheMFG.E_Commerce.repository.UserRepository;
import com.TheMFG.E_Commerce.service.Interface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId,userId);

        Cart cart = null;

        if(ObjectUtils.isEmpty(cartStatus)){
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        }else{
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }

        Cart saveCart = cartRepository.save(cart);

        return null;
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        return null;
    }
}
