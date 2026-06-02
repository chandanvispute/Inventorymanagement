package com.chandan.inventorymanagement.controller;

import com.chandan.inventorymanagement.entity.Product;
import com.chandan.inventorymanagement.repository.ProductRepository;
import com.chandan.inventorymanagement.service.ProductService;
import com.chandan.inventorymanagement.service.S3Service;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.amazonaws.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    public ProductController(ProductService productService,S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product createProduct(
            @ModelAttribute @Valid Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        System.out.println("asda");
        return productService.createProduct(product, image);
    }


    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product updateProduct(@PathVariable Long id,
                                 @Valid @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam Integer threshold) {
        return productService.getLowStockProducts(threshold);
    }

    @GetMapping("/top")
    public List<Product> getTopKProducts(@RequestParam int k) {
        return productService.getTopKExpensiveProducts(k);
    }

    @PostMapping("/bulk")
    public List<Product> createProducts(
            @Valid @RequestBody List<Product> products) {
        return productService.createProducts(products);
    }

    @PatchMapping("/addQuantity")
    public ResponseEntity<String> addQuantity(@RequestParam long productId, int quantity){
        productService.addQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
