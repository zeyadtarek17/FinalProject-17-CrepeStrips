package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderStatus; // Assuming you have this enum

public class CreatedStatusStrategy implements OrderStatusStrategy {
    @Override
    public String getStatusDetails(Order order) {
        if (order.getStatus() == OrderStatus.CREATED) {
            return "Your order (ID: " + order.getId() + ") has been received and is pending confirmation. We'll notify you soon!";
        }
        return "Order status is not CREATED."; // Fallback or error
    }
}