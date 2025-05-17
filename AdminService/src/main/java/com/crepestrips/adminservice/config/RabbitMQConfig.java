package com.crepestrips.adminservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue that receives reports from the user service
    public static final String USER_TO_ADMIN_QUEUE = "user_to_admin_report_queue";

    @Bean
    public Queue userToAdminQueue() {
        return new Queue(USER_TO_ADMIN_QUEUE, false); // durable false is okay
    }

    // Optional: Admin-specific queue/exchange if you're using it internally
    public static final String ADMIN_QUEUE_NAME = "adminServiceQueue";
    public static final String ADMIN_EXCHANGE_NAME = "adminServiceExchange";
    public static final String ADMIN_ROUTING_KEY = "adminServiceRoutingKey";

    @Bean
    public Queue adminServiceQueue() {
        return new Queue(ADMIN_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange adminServiceExchange() {
        return new TopicExchange(ADMIN_EXCHANGE_NAME);
    }

    @Bean
    public Binding adminServiceBinding() {
        return BindingBuilder.bind(adminServiceQueue())
                .to(adminServiceExchange())
                .with(ADMIN_ROUTING_KEY);
    }
}
