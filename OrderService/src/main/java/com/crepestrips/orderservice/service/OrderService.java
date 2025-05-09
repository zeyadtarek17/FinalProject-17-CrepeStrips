package com.crepestrips.orderservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    @Transactional
    public ResponseEntity<Order> createOrder(UUID userId, UUID restaurantId) {
        Order order = new Order(userId, restaurantId, null);
        // To-Do needs user microservice to get the cartId from the user and fetch the
        // cart items

        order = orderRepository.save(order);
        return ResponseEntity.ok(order);
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

    public ResponseEntity<Optional<List<Order>>> getOrdersByRestaurantId(UUID restaurantId) {
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

    //To-do add item to order

    // To-do remove item from order

}