package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import org.springframework.stereotype.Component;

@Component
public class UrgentPriorityStrategy implements OrderProcessingStrategy {
    @Override
    public void processOrder(Order order) {
        // Immediate processing logic for urgent priority orders
        System.out.println("Processing order " + order.getId() + " with URGENT priority");
        // Implement urgent priority business logic
    }
}