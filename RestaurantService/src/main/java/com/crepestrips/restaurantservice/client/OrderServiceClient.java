package com.crepestrips.restaurantservice.client;

import com.crepestrips.restaurantservice.dto.DefaultResult;
import com.crepestrips.restaurantservice.dto.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "orderservice", url = "http://orderservice:8084")
public interface OrderServiceClient {
    @GetMapping("/orders/restaurant/{restaurantId}")
    DefaultResult getOrdersByRestaurantId(@PathVariable("restaurantId") String restaurantId);
}