package com.chandan.inventorymanagement.service.impl;

import com.chandan.inventorymanagement.entity.Product;
import com.chandan.inventorymanagement.exception.ResourceNotFoundException;
import com.chandan.inventorymanagement.repository.ProductRepository;
import com.chandan.inventorymanagement.service.ProductService;
import com.chandan.inventorymanagement.service.S3Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final S3Service s3Service;

    // Constructor Injection (DI)
    public ProductServiceImpl(ProductRepository productRepository,S3Service s3Service) {
        this.s3Service = s3Service;
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product, MultipartFile image) {
        System.out.println("Entered");
        if (image != null && !image.isEmpty()) {
            System.out.println("Entered 1");
            String imageUrl = s3Service.uploadImage(image);
            System.out.println("end");
            System.out.println(imageUrl);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);
    }


    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id)
                );
    }

    @Override
    @Cacheable(value = "productList")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @CacheEvict(value = {"products", "productList"}, allEntries = true)
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);

        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existing);
    }

    @Override
    @CacheEvict(value = {"products", "productList"}, allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "getLowStockProducts", key="#threshold")
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }

    @Override
    @Cacheable(value = "getTopKExpensiveProducts", key="#k")
    public List<Product> getTopKExpensiveProducts(int k) {
        return productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> createProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

}