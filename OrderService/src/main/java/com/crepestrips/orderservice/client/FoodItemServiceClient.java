package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crepestrips.orderservice.dto.FoodItemResponse;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "fooditem-service", url = "${fooditem-service.url}")
public interface FoodItemServiceClient {

    // decrement food item stock
    @GetMapping("/api/users/decrementFoodItemStock")
    boolean decrementFoodItemStock(@RequestBody List<FoodItemResponse> foodItemIds);

}