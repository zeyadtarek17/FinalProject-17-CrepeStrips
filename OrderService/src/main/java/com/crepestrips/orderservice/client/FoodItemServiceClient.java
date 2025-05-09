package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "restaurant-service", url = "${restaurant-service.url}")
public interface FoodItemServiceClient {

    @GetMapping("/api/fooditems/{id}")
    Object getFoodItemById(@PathVariable("id") UUID id);


}