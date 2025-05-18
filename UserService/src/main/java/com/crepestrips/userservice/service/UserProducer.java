package com.crepestrips.userservice.service;

import com.crepestrips.userservice.config.RabbitMQConfig;
import com.crepestrips.userservice.dto.CartDto;
import com.crepestrips.userservice.dto.FoodItemResponse;
import com.crepestrips.userservice.dto.UserMessage;
import com.crepestrips.userservice.event.OrderPlacementRequestedEvent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

@Service
public class UserProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final String orderServiceExchange = "order.exchange";
    private final String orderServicePlacementRoutingKey = "user.to.order";

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void requestOrderPlacement(CartDto cartDetailsToPublish) {
        // 1. Create UserService's version of the OrderPlacementRequestedEvent
        OrderPlacementRequestedEvent eventToPublish = new OrderPlacementRequestedEvent(cartDetailsToPublish);

        try {
            logger.info("UserProducer: Publishing OrderPlacementRequestedEvent for cartId: {} to exchange '{}', routingKey '{}'",
                    cartDetailsToPublish.getCartId(), orderServiceExchange, orderServicePlacementRoutingKey);

            // 2. Send the event to RabbitMQ
            rabbitTemplate.convertAndSend(orderServiceExchange, orderServicePlacementRoutingKey, eventToPublish, message -> {
                // Set __TypeId__ to the fully qualified class name of OrderService's event class.
                // This tells the Jackson2JsonMessageConverter on the OrderService side which
                // specific Java class to deserialize the JSON message into.
                message.getMessageProperties().setHeader("__TypeId__", "com.crepestrips.orderservice.event.OrderPlacementRequestedEvent");
                
                // It's also good practice to explicitly set the content type.
                message.getMessageProperties().setContentType("application/json");
                return message;
            });

            logger.info("UserProducer: Successfully published OrderPlacementRequestedEvent. Event ID: {}",
                    eventToPublish.getEventId());

        } catch (Exception e) {
            logger.error("UserProducer: Failed to publish OrderPlacementRequestedEvent for cartId: {}. Error: {}",
                    cartDetailsToPublish.getCartId(), e.getMessage(), e);
            // Re-throw a runtime exception so the calling service (e.g., UserController)
            // can catch it and return an appropriate error response to the client.
            throw new RuntimeException("Failed to send order placement request to the message queue.", e);
        }
    }

    // public void createOrder(UUID userId, String restaurantId, List<FoodItemResponse> foodItems, UUID orderId) {
    //     UserMessage message = new UserMessage(userId, restaurantId, foodItems, orderId);
    //     rabbitTemplate.convertAndSend(
    //             RabbitMQConfig.EXCHANGE,
    //             RabbitMQConfig.ROUTING_KEY,
    //             message
    //     );
    //     System.out.println("Sent order to OrderService: " + orderId);
    // }
}
