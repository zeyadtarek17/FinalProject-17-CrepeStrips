package com.crepestrips.fooditemservice.config;

import com.crepestrips.fooditemservice.model.FoodItem;
import com.crepestrips.fooditemservice.service.FoodItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FoodItemConsumer {
    private final FoodItemService foodItemService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FoodItemConsumer(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @RabbitListener(queues = RabbitMQConfig.FOODITEM_QUEUE)
    public void handleNewFoodItems(String foodItemJson) {
        try {
            FoodItem item = objectMapper.readValue(foodItemJson, FoodItem.class);
            foodItemService.create(item);
            System.out.println("New Food Item created"+ item.getName());
        } catch (Exception e) {
            System.err.println("Error while creating food items: " + e.getMessage());
        }
    }
}
