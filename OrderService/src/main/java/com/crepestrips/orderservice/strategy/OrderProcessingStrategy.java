package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;

public interface OrderProcessingStrategy {
    void processOrder(Order order);
}