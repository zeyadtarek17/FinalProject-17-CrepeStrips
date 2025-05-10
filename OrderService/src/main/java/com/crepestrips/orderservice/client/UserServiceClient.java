package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    ResponseEntity<?> getUserById(@PathVariable("id") UUID id);
    
    @GetMapping("/api/users/{userId}/cart")
    ResponseEntity<?> getUserCart(@PathVariable("userId") UUID userId);
}