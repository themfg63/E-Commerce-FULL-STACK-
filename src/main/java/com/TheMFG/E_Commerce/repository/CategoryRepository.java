package com.TheMFG.E_Commerce.repository;

import com.TheMFG.E_Commerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public Boolean existsByName(String name);
}
