package com.crepestrips.adminservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_TO_ADMIN_QUEUE = "user_to_admin_report_queue";

    @Bean
    public Queue userToAdminQueue() {
        return new Queue(USER_TO_ADMIN_QUEUE, false);
    }
}
