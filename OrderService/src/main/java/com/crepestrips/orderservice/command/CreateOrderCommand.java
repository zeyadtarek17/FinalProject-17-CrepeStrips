package com.crepestrips.orderservice.command;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.repository.OrderRepository;
import com.crepestrips.orderservice.service.RabbitMQPublisher;

import java.util.UUID;

public class CreateOrderCommand implements OrderCommand {
    private final OrderRepository orderRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final UUID userId;
    private final String restaurantId;
    private Order createdOrder;

    public CreateOrderCommand(OrderRepository orderRepository, RabbitMQPublisher rabbitMQPublisher, 
                             UUID userId, String restaurantId) {
        this.orderRepository = orderRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    @Override
    public void execute() {
        createdOrder = new Order(userId, restaurantId, null);
        orderRepository.save(createdOrder);
        rabbitMQPublisher.publishOrderCreated(createdOrder);
    }

    @Override
    public void undo() {
        if (createdOrder != null) {
            orderRepository.delete(createdOrder);
        }
    }
    
    public Order getCreatedOrder() {
        return createdOrder;
    }
}