package com.chandan.inventorymanagement.service;

import com.chandan.inventorymanagement.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> createProducts(List<Product> products);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    List<Product> getLowStockProducts(Integer threshold);

    List<Product> getTopKExpensiveProducts(int k);

    Product createProduct(Product product, MultipartFile image);

}
