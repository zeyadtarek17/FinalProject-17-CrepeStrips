package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderStatus;

public class DeliveredStatusStrategy implements OrderStatusStrategy {
    @Override
    public String getStatusDetails(Order order) {
        if (order.getStatus() == OrderStatus.DELIVERED) {
            return "Your order (ID: " + order.getId() + ") has been successfully delivered. Enjoy your meal!";
        }
        return "Order status is not DELIVERED.";
    }
}