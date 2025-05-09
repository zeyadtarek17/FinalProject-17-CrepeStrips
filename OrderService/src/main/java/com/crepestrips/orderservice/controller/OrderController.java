package com.crepestrips.orderservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a basic order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam UUID userId, @RequestParam UUID restaurantId) {
        return orderService.createOrder(userId, restaurantId);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByUserId(@PathVariable UUID userId) {
        return orderService.getOrdersByUserId(userId);
    }

    // Get orders by restaurant ID
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByRestaurantId(@PathVariable UUID restaurantId) {
        return orderService.getOrdersByRestaurantId(restaurantId);
    }
    
    // Get orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }
    
    // Get orders by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByPriority(@PathVariable OrderPriority priority) {
        return orderService.getOrdersByPriority(priority);
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Optional<Order>> updateOrderStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

    // Prioritize order
    @PutMapping("/{id}/priority")
    public ResponseEntity<Optional<Order>> updateOrderPriority(@PathVariable UUID id, @RequestParam OrderPriority priority) {
        return orderService.updateOrderPriority(id, priority);
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Optional<Order>> deleteOrder(@PathVariable UUID id) {
        return orderService.deleteOrder(id);
    }

    // // Add item to order
    // @PostMapping("/{orderId}/items")
    // public ResponseEntity<Order> addItemToOrder(
    //         @PathVariable UUID orderId,
    //         @RequestParam UUID foodItemId,
    //         @RequestParam String foodItemName,
    //         @RequestParam int quantity,
    //         @RequestParam double price) {
    //     Order updatedOrder = orderService.addItemToOrder(orderId, foodItemId, foodItemName, quantity, price);
    //     return ResponseEntity.ok(updatedOrder);
    // }

    // // Remove item from order
    // @DeleteMapping("/{orderId}/items/{itemId}")
    // public ResponseEntity<Order> removeItemFromOrder(@PathVariable UUID orderId, @PathVariable UUID itemId) {
    //     Order updatedOrder = orderService.removeItemFromOrder(orderId, itemId);
    //     return ResponseEntity.ok(updatedOrder);
    // }
}