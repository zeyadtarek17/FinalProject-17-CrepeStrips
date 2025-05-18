package com.crepestrips.orderservice.config;

import com.crepestrips.orderservice.command.OrderCommandInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DesignPatternConfig {
    
    @Bean
    public OrderCommandInvoker orderCommandInvoker() {
        return new OrderCommandInvoker();
    }
}