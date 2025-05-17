package com.crepestrips.userservice.service;

import com.crepestrips.userservice.config.RabbitMQConfig;
import com.crepestrips.userservice.dto.FoodItemResponse;
import com.crepestrips.userservice.dto.UserMessage;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createOrder(UUID userId, String restaurantId, List<FoodItemResponse> foodItems, UUID orderId) {
        UserMessage message = new UserMessage(userId, restaurantId, foodItems, orderId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
        System.out.println("Sent order to OrderService: " + orderId);
    }
}
