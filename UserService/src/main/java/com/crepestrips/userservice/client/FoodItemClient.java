package com.crepestrips.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crepestrips.userservice.dto.FoodItemResponse;

import java.util.List;

@FeignClient(name = "food-item-service", url = "http://localhost:8082")
public interface FoodItemClient {

    @PostMapping("/fooditems/all")
    ResponseEntity<List<FoodItemResponse>> getItemsById(@RequestBody List<String> ids);


}
