package com.crepestrips.adminservice.client;



import com.crepestrips.adminservice.client.dto.FoodItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "food-item-service", url = "http://localhost:8082")
public interface FoodItemServiceClient {
    @GetMapping("fooditems/{id}")
    FoodItemDTO getFoodItem(@PathVariable String id);

    @PostMapping("/fooditems/{id}/suspend")
    void suspendFoodItem(@PathVariable String id);

    @PostMapping("/fooditems/{id}/activate")
    void activateFoodItem(@PathVariable String id);
}