package com.crepestrips.adminservice.client;



import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;


import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // For detailed logging
    }
}