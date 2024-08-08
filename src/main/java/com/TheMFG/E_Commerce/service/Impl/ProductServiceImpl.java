package com.TheMFG.E_Commerce.service.Impl;

import com.TheMFG.E_Commerce.model.Product;
import com.TheMFG.E_Commerce.repository.ProductRepository;
import com.TheMFG.E_Commerce.service.Interface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(Integer id){
        Product product = productRepository.findById(id).orElse(null);

        if(!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image)  {
        Product dbProduct = getProductById(product.getId());

        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
        dbProduct.setIsActive(product.getIsActive());
        dbProduct.setDiscount(product.getDiscount());

        Double discount = product.getPrice()*(product.getDiscount()/100.0);
        Double discountPrice = product.getPrice() - discount;
        dbProduct.setDiscountPrice(discountPrice);

        Product updateProduct = productRepository.save(dbProduct);

        if(!ObjectUtils.isEmpty(updateProduct)){
            if(!image.isEmpty()){
                try{
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "ürünler" + File.separator + image.getOriginalFilename());
                    Files.copy(image.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getAllActiveProducts(String category){
        List<Product> products = null;
        if(ObjectUtils.isEmpty(category)){
            products = productRepository.findByIsActiveTrue();
        }else{
            products = productRepository.findByCategory(category);
        }

        return products;
    }

    @Override
    public List<Product> searchProduct(String ch){
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch);
    }

    @Override
    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Product> pageProduct = null;

        if(ObjectUtils.isEmpty(category)){
            pageProduct = productRepository.findByIsActiveTrue(pageable);
        }else{
            pageProduct = productRepository.findByCategory(pageable,category);
        }
        return pageProduct;
    }

    @Override
    public Page<Product> searchProductPagination(Integer pageNo,Integer pageSize, String ch){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch,pageable);
    }

    @Override
    public Page<Product> getAllProductsPagination(Integer pageNo,Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findAll(pageable);
    }
}
