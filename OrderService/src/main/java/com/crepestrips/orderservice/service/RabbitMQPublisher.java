package com.crepestrips.orderservice.service;

import com.crepestrips.orderservice.config.RabbitMQConfig;
//import com.crepestrips.orderservice.dto.RestaurantOrderHistoryResponse;
import com.crepestrips.orderservice.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreated(Order order) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE, 
            RabbitMQConfig.ORDER_CREATED_ROUTING_KEY, 
            order
        );
    }

    public void publishOrderUpdated(Order order) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE, 
            RabbitMQConfig.ORDER_UPDATED_ROUTING_KEY, 
            order
        );
    }

//    public void publishOrderHistoryResponse(RestaurantOrderHistoryResponse response) {
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.ORDER_HISTORY_EXCHANGE,
//                RabbitMQConfig.ORDER_HISTORY_RESPONSE_KEY,
//                response
//        );
//    }
}