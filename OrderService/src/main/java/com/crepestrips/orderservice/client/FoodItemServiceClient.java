package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crepestrips.orderservice.dto.FoodItemDto;
import com.crepestrips.orderservice.dto.FoodItemServiceResponseDto;

import java.util.List;

@FeignClient(name = "fooditem-service", url = "${fooditem-service.url}")
public interface FoodItemServiceClient {

    @GetMapping("/fooditems/{id}")
    FoodItemServiceResponseDto getFoodItemByIdWrapped(@PathVariable("id") String id);

    // decrement food item stock
    @PostMapping("/fooditems/decrement")
    ResponseEntity<Boolean> decrementStock(@RequestBody List<String> ids);

}