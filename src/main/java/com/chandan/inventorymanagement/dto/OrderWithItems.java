package com.chandan.inventorymanagement.dto;

import com.chandan.inventorymanagement.entity.Order;
import com.chandan.inventorymanagement.entity.OrderItem;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Data
public class OrderWithItems {
    public Order order;
    public List<OrderItem> orderItems;
}
