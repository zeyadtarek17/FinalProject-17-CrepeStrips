package com.crepestrips.fooditemservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurantservice", url = "http://restaurantservice:8083")
public interface RestaurantServiceClient {

    @GetMapping("/available/{restaurantId}")
    Boolean isRestaurantAvailable(@PathVariable("restaurantId") String restaurantId);

}
