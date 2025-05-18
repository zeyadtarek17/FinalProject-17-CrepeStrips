package com.crepestrips.orderservice.strategy;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderProcessingContext {
    private final Map<OrderPriority, OrderProcessingStrategy> strategies = new HashMap<>();
    
    @Autowired
    public OrderProcessingContext(NormalPriorityStrategy normalStrategy, 
                                 HighPriorityStrategy highStrategy,
                                 UrgentPriorityStrategy urgentStrategy) {
        strategies.put(OrderPriority.NORMAL, normalStrategy);
        strategies.put(OrderPriority.HIGH, highStrategy);
        strategies.put(OrderPriority.URGENT, urgentStrategy);
        // Default strategy for LOW priority can be the same as NORMAL
        strategies.put(OrderPriority.LOW, normalStrategy);
    }
    
    public void processOrder(Order order) {
        OrderProcessingStrategy strategy = strategies.get(order.getPriority());
        if (strategy == null) {
            // Fallback to normal strategy if priority doesn't match
            strategy = strategies.get(OrderPriority.NORMAL);
        }
        strategy.processOrder(order);
    }
}