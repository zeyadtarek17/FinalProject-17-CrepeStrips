package com.crepestrips.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/user/{id}")
    Object getUserById(@PathVariable("id") Long id);

}