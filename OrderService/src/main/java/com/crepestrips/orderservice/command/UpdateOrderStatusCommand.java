package com.crepestrips.orderservice.command;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;

public class UpdateOrderStatusCommand implements OrderCommand {
    private final OrderRepository orderRepository;
    private final UUID orderId;
    private final OrderStatus newStatus;
    private OrderStatus previousStatus;
    private Order order;

    public UpdateOrderStatusCommand(OrderRepository orderRepository, UUID orderId, OrderStatus newStatus) {
        this.orderRepository = orderRepository;
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    @Override
    public void execute() {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            order = orderOptional.get();
            previousStatus = order.getStatus();
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    @Override
    public void undo() {
        if (order != null && previousStatus != null) {
            order.setStatus(previousStatus);
            orderRepository.save(order);
        }
    }
    
    public Order getOrder() {
        return order;
    }
}