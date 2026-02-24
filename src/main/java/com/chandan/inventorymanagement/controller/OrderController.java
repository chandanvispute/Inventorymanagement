package com.chandan.inventorymanagement.controller;

import com.chandan.inventorymanagement.dto.OrderRequest;
import com.chandan.inventorymanagement.entity.Order;
import com.chandan.inventorymanagement.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @DeleteMapping("/{orderId}")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    public Page<Order> getAllOrders(
            @PageableDefault(
                    size = 5,
                    sort = "orderDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return orderService.getAllOrders(pageable);
    }
}
