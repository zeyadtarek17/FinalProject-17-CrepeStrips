package com.crepestrips.orderservice.service;

import com.crepestrips.orderservice.config.RabbitMQConfig;
import com.crepestrips.orderservice.dto.UserMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.USER_TO_ORDER_QUEUE)
    public void handleUserMessage(UserMessage message) {
        System.out.println("Received user ID from UserService: " + message.getUserId());
    }
}
