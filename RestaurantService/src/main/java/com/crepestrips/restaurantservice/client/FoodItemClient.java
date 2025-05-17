package com.crepestrips.restaurantservice.client;

import com.crepestrips.restaurantservice.dto.DefaultResult;
import com.crepestrips.restaurantservice.dto.FoodItemDTO;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "food-item-service", url="http://localhost:8082")
public interface FoodItemClient {
    @PostMapping("/fooditems")
    DefaultResult createFoodItem(@RequestBody FoodItemDTO foodItemDTO);

    @PutMapping("/fooditems/{id}")
    DefaultResult updateFoodItem(@PathVariable String id, @RequestBody FoodItemDTO foodItemDTO);

    @DeleteMapping("/fooditems/{id}")
    DefaultResult deleteFoodItem(@PathVariable String id);
    @GetMapping("/fooditems/getAllFoodItems/{restaurantId}")
    DefaultResult getFoodItemsByRestaurantId(@PathVariable("restaurantId") String restaurantId);
}
