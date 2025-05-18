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
import com.crepestrips.orderservice.client.FoodItemServiceClient;
import com.crepestrips.orderservice.config.RabbitMQConfig;
// import com.crepestrips.orderservice.dto.FoodItemResponse;
// import com.crepestrips.orderservice.dto.UserMessage;
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.repository.OrderItemRepository;
import com.crepestrips.orderservice.repository.OrderRepository;

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

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            RabbitMQPublisher rabbitMQPublisher, FoodItemServiceClient foodItemServiceClient,
            UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
        this.foodItemServiceClient = foodItemServiceClient;
        this.userServiceClient = userServiceClient;

    }

    // @Transactional
    // public ResponseEntity<Order> createOrder(UUID userId, S restaurantId) {
    //     Order order = new Order(userId, restaurantId, null);
    //     ResponseEntity<?> response = userServiceClient.getUserCart(userId);
    //     if (response.getStatusCode().is2xxSuccessful()) {
    //         ResponseEntity<?> cartResponse = userServiceClient.getUserCart(userId);
    //         // to do when i have cart data is that i first need to create an order item for
    //         // each item in the cart and then add them to the order
    //     }
    //     order = orderRepository.save(order);
    //     rabbitMQPublisher.publishOrderCreated(order);
    //     return ResponseEntity.ok(order);
    // }

    // @RabbitListener(queues = RabbitMQConfig.USER_TO_ORDER_QUEUE)
    // public UUID createOrder(UUID userId, String restaurantId,
    // List<FoodItemResponse> foodItems) {
    // List<String> foodItemsIds = foodItems.stream()
    // .map(FoodItemResponse::getId)
    // .collect(Collectors.toList());

    // // send to endpoint to decrement the food item stock (sync)
    // boolean success =
    // foodItemServiceClient.decrementStock(foodItemsIds).getBody();
    // if (!success) {
    // throw new RuntimeException("Stock is being updated");
    // }
    // Order order = new Order(userId, restaurantId, foodItems);
    // order.calculateTotalAmount();
    // orderRepository.save(order);
    // return order.getId();
    // }

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
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setStatus(status);
            orderRepository.save(order.get());
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
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

    // To-do add item to order

    // To-do remove item from order

    // added by team1
    @RabbitListener(queues = RabbitMQConfig.ORDER_HISTORY_REQUEST_QUEUE)
    public void handleOrderHistoryRequest(RestaurantOrderHistoryRequest request) {
        String restaurantUUID = request.getRestaurantId();
        Optional<List<Order>> orders = orderRepository.findByRestaurantId(restaurantUUID);

        RestaurantOrderHistoryResponse response = new RestaurantOrderHistoryResponse();
        response.setRestaurantId(request.getRestaurantId());
        response.setOrders(orders.orElse(List.of()));

        rabbitMQPublisher.publishOrderHistoryResponse(response);
    }

}