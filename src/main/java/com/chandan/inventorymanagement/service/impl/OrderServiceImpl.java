package com.chandan.inventorymanagement.service.impl;

import com.chandan.inventorymanagement.dto.OrderItemRequest;
import com.chandan.inventorymanagement.dto.OrderRequest;
import com.chandan.inventorymanagement.dto.OrderWithItems;
import com.chandan.inventorymanagement.entity.Order;
import com.chandan.inventorymanagement.entity.OrderItem;
import com.chandan.inventorymanagement.entity.Product;
import com.chandan.inventorymanagement.exception.ResourceNotFoundException;
import com.chandan.inventorymanagement.repository.OrderItemRepository;
import com.chandan.inventorymanagement.repository.OrderRepository;
import com.chandan.inventorymanagement.repository.ProductRepository;
import com.chandan.inventorymanagement.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        // 🔁 Algorithm: validate & calculate
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            // Deduct stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity());

            productRepository.save(product);

            // Create order item
            OrderItem orderItem = new OrderItem(
                    product,
                    itemRequest.getQuantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemRequest.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        for(OrderItem i:orderItems){
            i.setOrder(order);
            orderItemRepository.save(i);
        }

        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + orderId));

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Only CREATED orders can be cancelled");
        }

        List<OrderItem> items = orderItemRepository.findByOrder(order);
        for (OrderItem item : items) {

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found: " + item.getProduct().getId()));

            product.setStockQuantity(
                    product.getStockQuantity() + item.getQuantity());

            productRepository.save(product);
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public OrderWithItems getOrderById(Long orderId) {
        OrderWithItems orderWithItems = new OrderWithItems();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found: " + orderId));
        orderWithItems.setOrder(order);
        orderWithItems.setOrderItems(orderItemRepository.findByOrder(order));
        return orderWithItems;
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
