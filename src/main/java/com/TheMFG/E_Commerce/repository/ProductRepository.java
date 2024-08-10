package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByIsActiveTrue();
    Page<Product> findByIsActiveTrue(Pageable pageable);
    List<Product> findByCategory(String category);
    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);
    Page<Product> findByCategory(Pageable pageable,String category);
    Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2,Pageable pageable);
    Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,Pageable pageable);
}
