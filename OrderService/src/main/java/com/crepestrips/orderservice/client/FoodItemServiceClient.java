package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@FeignClient(name = "fooditem-service", url = "${fooditem-service.url}")
public interface FoodItemServiceClient {

    // decrement food item stock
    @PostMapping("/fooditems/decrement")
    ResponseEntity<Boolean> decrementStock(@RequestBody List<String> ids);

}