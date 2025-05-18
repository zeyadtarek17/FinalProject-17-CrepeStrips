package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import org.springframework.stereotype.Component;

@Component
public class HighPriorityStrategy implements OrderProcessingStrategy {
    @Override
    public void processOrder(Order order) {
        // Expedited processing logic for high priority orders
        System.out.println("Processing order " + order.getId() + " with HIGH priority");
        // Implement high priority business logic
    }
}