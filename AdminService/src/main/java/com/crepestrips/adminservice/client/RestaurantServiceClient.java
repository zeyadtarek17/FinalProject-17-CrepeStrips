package com.crepestrips.adminservice.client;


import com.crepestrips.adminservice.client.dto.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "http://localhost:8082")
public interface RestaurantServiceClient {
    @GetMapping("/api/restaurants/{id}")
    RestaurantDTO getRestaurant(@PathVariable Long id);

    @PostMapping("/api/restaurants/{id}/ban")
    void banRestaurant(@PathVariable Long id);

    @PostMapping("/api/restaurants/{id}/activate")
    void activateRestaurant(@PathVariable Long id);
}