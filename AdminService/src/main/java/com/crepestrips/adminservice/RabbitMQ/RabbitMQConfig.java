package com.crepestrips.adminservice.RabbitMQ;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "adminServiceQueue";
    public static final String EXCHANGE_NAME = "adminServiceExchange";
    public static final String ROUTING_KEY = "adminServiceRoutingKey";

    // Declare a Queue
    @Bean
    public Queue adminServiceQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // Declare a Topic Exchange
    @Bean
    public TopicExchange adminServiceExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Bind the Queue to the Exchange with a Routing Key
    @Bean
    public Binding adminServiceBinding() {
        return BindingBuilder.bind(adminServiceQueue())
                .to(adminServiceExchange())
                .with(ROUTING_KEY);
    }
}

