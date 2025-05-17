package com.crepestrips.restaurantservice.client;

import com.crepestrips.restaurantservice.dto.FoodItemDTO;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "food-item-service", url="http://localhost:8082")
public interface FoodItemClient {
    @PostMapping("/fooditems")
    FoodItemDTO createFoodItem(@RequestBody FoodItemDTO foodItemDTO);

    @PutMapping("/fooditems/{id}")
    FoodItemDTO updateFoodItem(@PathVariable String id, @RequestBody FoodItemDTO foodItemDTO);

    @DeleteMapping("/fooditems/{id}")
    void deleteFoodItem(@PathVariable String id);
    @GetMapping("/fooditems/getAllFoodItems/{restaurantId}")
    List<FoodItemDTO> getFoodItemsByRestaurantId(@PathVariable("restaurantId") String restaurantId);
}
