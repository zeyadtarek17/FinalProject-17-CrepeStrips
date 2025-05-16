package com.crepestrips.adminservice.client;


import com.crepestrips.adminservice.client.dto.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "http://localhost:8083")
public interface RestaurantServiceClient {
    @GetMapping("/restaurants/{id}")
    RestaurantDTO getRestaurant(@PathVariable Long id);

    @PostMapping("/restaurants/{id}/ban")
    void banRestaurant(@PathVariable Long id);

    @PostMapping("/restaurants/{id}/unban")
    void activateRestaurant(@PathVariable Long id);
}