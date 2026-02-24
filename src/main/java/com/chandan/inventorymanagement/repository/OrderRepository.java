package com.chandan.inventorymanagement.repository;

import java.util.Optional;
import com.chandan.inventorymanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);
}
