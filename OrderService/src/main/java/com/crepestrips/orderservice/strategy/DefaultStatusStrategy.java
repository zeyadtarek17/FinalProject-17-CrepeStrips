package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;

public class DefaultStatusStrategy implements OrderStatusStrategy {
    @Override
    public String getStatusDetails(Order order) {
        return "Your order (ID: " + order.getId() + ") is currently: " + order.getStatus().toString() + ".";
    }
}