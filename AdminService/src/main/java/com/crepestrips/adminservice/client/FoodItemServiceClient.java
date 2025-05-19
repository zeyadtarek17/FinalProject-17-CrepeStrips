package com.crepestrips.adminservice.client;



import com.crepestrips.adminservice.client.dto.FoodItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "fooditemservice", url = "http://fooditemservice:8082")
public interface FoodItemServiceClient {
    @GetMapping("/fooditems/{id}")
    FoodItemDTO getFoodItem(@PathVariable String id);

    @PutMapping("/fooditems/{id}/suspend")
    void suspendFoodItem(@PathVariable String id);

    @PutMapping("/fooditems/{id}/unsuspend")
    void activateFoodItem(@PathVariable String id);
}