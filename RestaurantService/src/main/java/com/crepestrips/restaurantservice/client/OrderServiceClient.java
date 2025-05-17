package com.crepestrips.restaurantservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "orderservice", url = "http://localhost:8082")
public interface OrderServiceClient {
    @GetMapping("/api/orders/restaurant/{restaurantId}")
    List<Order> getOrdersByRestaurantId(@PathVariable("restaurantId") String restaurantId);
}
