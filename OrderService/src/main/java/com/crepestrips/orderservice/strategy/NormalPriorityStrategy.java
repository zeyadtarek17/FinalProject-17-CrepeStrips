package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import org.springframework.stereotype.Component;

@Component
public class NormalPriorityStrategy implements OrderProcessingStrategy {
    @Override
    public void processOrder(Order order) {
        // Standard processing logic for normal priority orders
        System.out.println("Processing order " + order.getId() + " with NORMAL priority");
        // Implement normal priority business logic
    }
}