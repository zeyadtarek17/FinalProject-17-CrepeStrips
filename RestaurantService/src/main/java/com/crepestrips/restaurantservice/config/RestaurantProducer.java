package com.crepestrips.restaurantservice.config;

import com.crepestrips.restaurantservice.dto.FoodItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.crepestrips.restaurantservice.config.RabbitMQConfig.EXCHANGE;
import static com.crepestrips.restaurantservice.config.RabbitMQConfig.FOODITEM_ROUTING_KEY;

@Service
public class RestaurantProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public RestaurantProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendNewFoodItem(FoodItemDTO dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend(EXCHANGE, FOODITEM_ROUTING_KEY, json);
            System.out.println(" Sent food item to FoodItemService: " + dto.getName());
        } catch (Exception e) {
            System.err.println(" Failed to send food item: " + e.getMessage());
        }

    }
    public void sendFoodItemCommand(FoodItemMessage message) {
        try {
            String json= objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(EXCHANGE, FOODITEM_ROUTING_KEY, json);
            System.out.println(" Sent food item to FoodItemService: " + message.getAction());
        } catch (Exception e) {
            System.err.println(" Failed to send food item: " + e.getMessage());
        }
    }

}
