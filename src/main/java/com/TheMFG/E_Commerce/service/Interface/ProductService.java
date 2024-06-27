package com.TheMFG.E_Commerce.service.Interface;

import com.TheMFG.E_Commerce.model.Product;

import java.util.List;

public interface ProductService {
    public Product saveProduct(Product product);

    public List<Product> getAllProducts()    ;

    public Boolean deleteProduct(Integer id);
}
