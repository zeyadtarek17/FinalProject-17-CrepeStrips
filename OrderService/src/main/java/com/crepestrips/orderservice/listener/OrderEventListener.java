package com.crepestrips.orderservice.listener;

import com.crepestrips.orderservice.dto.CartDto;
import com.crepestrips.orderservice.event.OrderPlacementRequestedEvent;
import com.crepestrips.orderservice.service.OrderCreationService; // Import the new service
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    private final OrderCreationService orderCreationService; // Inject the service

    @Autowired
    public OrderEventListener(OrderCreationService orderProcessingService) { // Constructor injection
        this.orderCreationService = orderProcessingService;
    }

    @RabbitListener(queues = "${app.rabbitmq.orderPlacementQueue}")
    public void handleOrderPlacementRequest(OrderPlacementRequestedEvent event) {
        logger.info("============================================================");
        logger.info("||          RECEIVED NEW ORDER PLACEMENT REQUEST          ||");
        // ... (keep your existing detailed logging of the received event) ...
        logger.info("Full Event Received: {}", event);

        if (event == null || event.getCartDetails() == null) {
            logger.error("|| ERROR: Received invalid event or event with null cart details. ||");
            logger.info("============================================================");
            return;
        }
        logger.info("|| Event ID: {}", event.getEventId());
        logger.info("|| Event Timestamp: {}", event.getEventTimestamp());
        CartDto cartDetails = event.getCartDetails();
        logger.info("|| --- Cart Details ---");
        logger.info("|| Cart ID: {}", cartDetails.getCartId());
        logger.info("|| User ID: {}", cartDetails.getUserId());
        logger.info("|| Food Item IDs: {}", cartDetails.getFoodItemIds());
        logger.info("============================================================");


        try {
            // Delegate processing to the service
            orderCreationService.processOrderPlacement(cartDetails);
            logger.info("Successfully processed order placement for cartId: {}", cartDetails.getCartId());
        } catch (Exception e) {
            logger.error("Error processing order placement for cartId: {}. Error: {}", cartDetails.getCartId(), e.getMessage(), e);
            // TODO: Implement proper error handling, e.g., send to Dead Letter Queue (DLQ)
            // For now, the message might be re-queued or dropped depending on RabbitMQ/Spring AMQP config
        }
    }
}