package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderStatus;

public class PreparingStatusStrategy implements OrderStatusStrategy {
    @Override
    public String getStatusDetails(Order order) {
        if (order.getStatus() == OrderStatus.PREPARING) {
            // In a real app, you might fetch an estimated completion time here
            return "Great news! Your order (ID: " + order.getId() + ") is currently being prepared by the restaurant.";
        }
        return "Order status is not PREPARING.";
    }
}