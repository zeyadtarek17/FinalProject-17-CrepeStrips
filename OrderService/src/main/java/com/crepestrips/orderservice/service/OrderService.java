package com.crepestrips.orderservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.crepestrips.orderservice.client.FoodItemServiceClient;
import com.crepestrips.orderservice.client.FoodItemServiceClient;
import com.crepestrips.orderservice.config.RabbitMQConfig;
import com.crepestrips.orderservice.dto.DefaultResult;
// import com.crepestrips.orderservice.dto.FoodItemResponse;
// import com.crepestrips.orderservice.dto.UserMessage;
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.repository.OrderItemRepository;
import com.crepestrips.orderservice.repository.OrderRepository;
import com.crepestrips.orderservice.strategy.CreatedStatusStrategy;
import com.crepestrips.orderservice.strategy.DefaultStatusStrategy;
import com.crepestrips.orderservice.strategy.DeliveredStatusStrategy;
import com.crepestrips.orderservice.strategy.OrderStatusStrategy;
import com.crepestrips.orderservice.strategy.PreparingStatusStrategy;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private FoodItemServiceClient foodItemServiceClient;

    private final Map<OrderStatus, OrderStatusStrategy> statusStrategies;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            RabbitMQPublisher rabbitMQPublisher, FoodItemServiceClient foodItemServiceClient) {
        this.orderRepository = orderRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.foodItemServiceClient = foodItemServiceClient;
        statusStrategies = new HashMap<>();
        statusStrategies.put(OrderStatus.CREATED, new CreatedStatusStrategy());
        statusStrategies.put(OrderStatus.PREPARING, new PreparingStatusStrategy());
        statusStrategies.put(OrderStatus.DELIVERED, new DeliveredStatusStrategy());
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId).orElse(null);
    }

    public List<Order> getOrdersByRestaurantId(String restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).orElse(null);
    }

    public List<Order> getOrdersByPriority(OrderPriority priority) {
        return orderRepository.findByPriority(priority).orElse(null);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).orElse(null);
    }

    public OrderStatus getOrderStatus(UUID id) {
        return orderRepository.findById(id).map(Order::getStatus).orElse(null);
    }

    @Transactional
    public Order updateOrderPriority(UUID id, OrderPriority priority) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setPriority(priority);
            return orderRepository.save(order.get());
        }
        return null;
    }

    @Transactional
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setStatus(status);
            return orderRepository.save(order.get());
        }
        return null;
    }

    public String getOrderStatusDetails(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return "Order with ID " + orderId + " not found.";
        }

        Order order = orderOptional.get();
        OrderStatusStrategy strategy = statusStrategies.getOrDefault(order.getStatus(), new DefaultStatusStrategy());
        return strategy.getStatusDetails(order);
    }

    @Transactional
    public Order deleteOrder(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.delete(order.get());
            return order.get();
        }
        return null;
    }
}
