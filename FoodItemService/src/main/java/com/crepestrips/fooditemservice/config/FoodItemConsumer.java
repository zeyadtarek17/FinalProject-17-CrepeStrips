package com.crepestrips.fooditemservice.config;

import com.crepestrips.fooditemservice.dto.FoodItemDTO;
import com.crepestrips.fooditemservice.model.FoodItem;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FoodItemConsumer {
    @RabbitListener(queues= RabbitMQConfig.FOOD_ITEM_QUEUE)
    public void consume(FoodItemDTO item) {
        System.out.println("Recieved in FoodItem : " + item.getName());

    }
}
