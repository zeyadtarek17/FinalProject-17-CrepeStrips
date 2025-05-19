package com.crepestrips.userservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crepestrips.userservice.dto.DefaultResult;

@FeignClient(name = "fooditemservice", url = "http://fooditemservice:8082")
public interface FoodItemServiceClient {

    @PostMapping("/fooditems/clean-cart")
    DefaultResult cleanCart(@RequestBody List<String> ids);

}
