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


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByUserId(@PathVariable UUID userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByRestaurantId(@PathVariable String restaurantId) {
        return orderService.getOrdersByRestaurantId(restaurantId);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Optional<List<Order>>> getOrdersByPriority(@PathVariable OrderPriority priority) {
        return orderService.getOrdersByPriority(priority);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Optional<Order>> updateOrderStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<Optional<Order>> updateOrderPriority(@PathVariable UUID id, @RequestParam OrderPriority priority) {
        return orderService.updateOrderPriority(id, priority);
    }

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