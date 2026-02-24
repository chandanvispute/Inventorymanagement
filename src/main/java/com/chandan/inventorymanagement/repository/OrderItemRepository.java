package com.chandan.inventorymanagement.repository;

import com.chandan.inventorymanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
