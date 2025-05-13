package com.crepestrips.adminservice.client;



import com.crepestrips.adminservice.client.dto.FoodItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "food-item-service", url = "http://localhost:8082")
public interface FoodItemServiceClient {
    @GetMapping("/api/food-items/{id}")
    FoodItemDTO getFoodItem(@PathVariable Long id);

    @PostMapping("/api/food-items/{id}/suspend")
    void suspendFoodItem(@PathVariable Long id);

    @PostMapping("/api/food-items/{id}/activate")
    void activateFoodItem(@PathVariable Long id);
}