package com.crepestrips.restaurantservice.config;


import com.crepestrips.restaurantservice.dto.FoodItemDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendFoodItem(FoodItemDTO item) {
        rabbitTemplate.convertAndSend(
                com.crepestrips.fooditemservice.config.RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.FOOD_ITEM_ROUTING_KEY,
                item
        );
        System.out.println("Sending from Restaurant: " + item.getName());

    }
}
