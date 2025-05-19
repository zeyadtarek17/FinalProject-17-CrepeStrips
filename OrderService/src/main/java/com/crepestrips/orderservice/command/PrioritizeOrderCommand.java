package com.crepestrips.orderservice.command;
import java.util.UUID;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.service.OrderService; 

public class PrioritizeOrderCommand implements OrderCommand {
    private final OrderService orderService; // The "Receiver"
    private final UUID orderId;
    private final OrderPriority newPriority;

    public PrioritizeOrderCommand(OrderService orderService, UUID orderId, OrderPriority newPriority) {
        this.orderService = orderService;
        this.orderId = orderId;
        this.newPriority = newPriority;
    }

    @Override
    public void execute() {
        orderService.updateOrderPriority(orderId, newPriority);
    }
}