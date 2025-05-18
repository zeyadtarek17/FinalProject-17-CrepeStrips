package com.crepestrips.orderservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.crepestrips.orderservice.client.FoodItemServiceClient;
import com.crepestrips.orderservice.client.UserServiceClient;
import com.crepestrips.orderservice.command.OrderCommandInvoker;
import com.crepestrips.orderservice.command.UpdateOrderStatusCommand;
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.repository.OrderItemRepository;
import com.crepestrips.orderservice.repository.OrderRepository;
import com.crepestrips.orderservice.strategy.OrderProcessingContext;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private FoodItemServiceClient foodItemServiceClient;

    @Autowired
    private OrderCommandInvoker commandInvoker;

    @Autowired
    private OrderProcessingContext processingContext;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            RabbitMQPublisher rabbitMQPublisher, FoodItemServiceClient foodItemServiceClient,
            UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.foodItemServiceClient = foodItemServiceClient;
        this.userServiceClient = userServiceClient;

    }

    public ResponseEntity<?> getOrderById(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<Optional<List<Order>>> getOrdersByUserId(UUID userId) {
        Optional<List<Order>> orders = orderRepository.findByUserId(userId);
        if (orders.isPresent()) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Optional<List<Order>>> getOrdersByRestaurantId(String restaurantId) {
        Optional<List<Order>> orders = orderRepository.findByRestaurantId(restaurantId);
        if (orders.isPresent()) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Optional<List<Order>>> getOrdersByPriority(OrderPriority priority) {
        Optional<List<Order>> orders = orderRepository.findByPriority(priority);
        if (orders.isPresent()) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Optional<List<Order>>> getOrdersByStatus(OrderStatus status) {
        Optional<List<Order>> orders = orderRepository.findByStatus(status);
        if (orders.isPresent()) {
            return ResponseEntity.ok(orders);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Optional<Order>> updateOrderPriority(UUID id, OrderPriority priority) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setPriority(priority);
            orderRepository.save(order.get());
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Optional<Order>> updateOrderStatus(UUID id, OrderStatus status) {
        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(orderRepository, id, status);
        commandInvoker.executeCommand(command);
        Order order = command.getOrder();
        if (order != null) {
            processingContext.processOrder(order);
            return ResponseEntity.ok(Optional.of(order));
        }
        Optional<Order> orderReturned = Optional.ofNullable(order);
        return ResponseEntity.ok(orderReturned);
       

    }

    @Transactional
    public ResponseEntity<Optional<Order>> deleteOrder(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.delete(order.get());
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

}