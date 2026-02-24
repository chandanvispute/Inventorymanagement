package com.chandan.inventorymanagement.service;

import com.chandan.inventorymanagement.dto.OrderRequest;
import com.chandan.inventorymanagement.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Order placeOrder(OrderRequest orderRequest);

    Order cancelOrder(Long orderId);

    Order getOrderById(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);
}
