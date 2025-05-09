package com.crepestrips.fooditemservice.config;

import com.crepestrips.fooditemservice.dto.FoodItemDTO;
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
    @RabbitListener(queues = RabbitMQConfig.FOODITEM_QUEUE)
    public void handleFoodItemMessage(String json) {
        try {
            FoodItemMessage message = objectMapper.readValue(json, FoodItemMessage.class);
            String action = message.getAction();
            String id = message.getFoodItemId();
            FoodItemDTO dto = message.getPayload();

            switch (action) {
                case "CREATE" -> {
                    FoodItem item = objectMapper.convertValue(dto, FoodItem.class);
                    foodItemService.create(item);
                    System.out.println("✅ Created: " + item.getName());
                }
                case "UPDATE" -> {
                    if (id != null && dto != null) {
                        FoodItem item = objectMapper.convertValue(dto, FoodItem.class);
                        foodItemService.update(id, item);
                        System.out.println("✅ Updated: " + id);
                    }
                }
                case "DELETE" -> {
                    if (id != null) {
                        foodItemService.delete(id);
                        System.out.println("✅ Deleted: " + id);
                    }
                }
                default -> System.err.println("⚠️ Unknown action: " + action);
            }

        } catch (Exception e) {
            System.err.println("❌ Error handling message: " + e.getMessage());
        }
    }
}
