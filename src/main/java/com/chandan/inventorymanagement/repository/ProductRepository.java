package com.chandan.inventorymanagement.repository;

import com.chandan.inventorymanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // No code needed – CRUD comes for free
    List<Product> findByStockQuantityLessThan(Integer threshold);
}
