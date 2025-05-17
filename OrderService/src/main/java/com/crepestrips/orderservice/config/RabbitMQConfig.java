package com.crepestrips.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_UPDATED_QUEUE = "order.updated.queue";
    
    public static final String ORDER_EXCHANGE = "order.exchange";
    
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String ORDER_UPDATED_ROUTING_KEY = "order.updated";
    public static final String USER_TO_ORDER_QUEUE = "user.to.order.queue";
    public static final String USER_TO_ORDER_ROUTING_KEY = "user.to.order";

    public static final String ORDER_HISTORY_EXCHANGE = "restaurant.order.exchange";
    public static final String ORDER_HISTORY_REQUEST_QUEUE = "restaurant.order.request.queue";
    public static final String ORDER_HISTORY_RESPONSE_QUEUE = "restaurant.order.response.queue";
    public static final String ORDER_HISTORY_REQUEST_KEY = "restaurant.order.request";
    public static final String ORDER_HISTORY_RESPONSE_KEY = "restaurant.order.response";

    @Bean
    public Queue userToOrderQueue() {
        return new Queue(USER_TO_ORDER_QUEUE);
    }

    @Bean
    public Binding userToOrderBinding() {
        return BindingBuilder
                .bind(userToOrderQueue())
                .to(orderExchange())
                .with(USER_TO_ORDER_ROUTING_KEY);
    }


    // Create queues
    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public Queue orderUpdatedQueue() {
        return new Queue(ORDER_UPDATED_QUEUE);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // Bind queues to exchange with routing keys
    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(orderExchange())
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding orderUpdatedBinding() {
        return BindingBuilder
                .bind(orderUpdatedQueue())
                .to(orderExchange())
                .with(ORDER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Queue orderHistoryRequestQueue() {
        return new Queue(ORDER_HISTORY_REQUEST_QUEUE);
    }

    @Bean
    public Queue orderHistoryResponseQueue() {
        return new Queue(ORDER_HISTORY_RESPONSE_QUEUE);
    }

    @Bean
    public TopicExchange orderHistoryExchange() {
        return new TopicExchange(ORDER_HISTORY_EXCHANGE);
    }

    @Bean
    public Binding orderHistoryRequestBinding() {
        return BindingBuilder
                .bind(orderHistoryRequestQueue())
                .to(orderHistoryExchange())
                .with(ORDER_HISTORY_REQUEST_KEY);
    }

    @Bean
    public Binding orderHistoryResponseBinding() {
        return BindingBuilder
                .bind(orderHistoryResponseQueue())
                .to(orderHistoryExchange())
                .with(ORDER_HISTORY_RESPONSE_KEY);
    }

    // Message converter
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configure RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}