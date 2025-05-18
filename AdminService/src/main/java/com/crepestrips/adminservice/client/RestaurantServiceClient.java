package com.crepestrips.adminservice.client;


import com.crepestrips.adminservice.client.dto.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurantservice", url = "http://restaurantservice:8083")
public interface RestaurantServiceClient {
    @GetMapping("/restaurants/{id}")
    RestaurantDTO getRestaurant(@PathVariable String id);

    @PutMapping("/restaurants/{id}/ban")
    void banRestaurant(@PathVariable String id);

    @PutMapping("/restaurants/{id}/unban")
    void activateRestaurant(@PathVariable String id);
}