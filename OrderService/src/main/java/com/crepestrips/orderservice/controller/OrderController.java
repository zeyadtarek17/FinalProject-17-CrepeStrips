package com.crepestrips.orderservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.crepestrips.orderservice.command.PrioritizeOrderCommand;
import com.crepestrips.orderservice.dto.DefaultResult;
import com.crepestrips.orderservice.command.OrderCommand;
import com.crepestrips.orderservice.model.Order;
import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import com.crepestrips.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResult> getOrderById(@PathVariable UUID id) {
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(new DefaultResult("Order: " + id + " found", false, order));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping
    public ResponseEntity<DefaultResult> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(new DefaultResult("All orders found", false, orders));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultResult> getOrdersByUserId(@PathVariable UUID userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(new DefaultResult("Orders for user: " + userId + " found", false, orders));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<DefaultResult> getOrdersByRestaurantId(@PathVariable String restaurantId) {
        try {
            List<Order> orders = orderService.getOrdersByRestaurantId(restaurantId);
            return ResponseEntity
                    .ok(new DefaultResult("Orders for restaurant: " + restaurantId + " found", false, orders));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<DefaultResult> getOrdersByStatus(@PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(new DefaultResult("Orders with status: " + status + " found", false, orders));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<DefaultResult> getOrdersByPriority(@PathVariable OrderPriority priority) {
        try {
            List<Order> orders = orderService.getOrdersByPriority(priority);
            return ResponseEntity.ok(new DefaultResult("Orders with priority: " + priority + " found", false, orders));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DefaultResult> updateOrderStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(new DefaultResult("Order: " + id + " status updated to " + status, false, order));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<DefaultResult> updateOrderPriority(@PathVariable UUID orderId,
            @RequestParam String priority) {
        OrderPriority newPriority;
        try {
            newPriority = OrderPriority.valueOf(priority.toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.ok((new DefaultResult("Invalid priority value", true, null)));
        }
        OrderCommand command = new PrioritizeOrderCommand(orderService, orderId, newPriority);
        command.execute();
        return ResponseEntity.ok(new DefaultResult("Order: " + orderId + " priority updated to " + newPriority, false,
                null));
    }

    @GetMapping("/{orderId}/status-details")
    public ResponseEntity<DefaultResult> getOrderStatusDescription(@PathVariable UUID orderId) {
        try {
            String statusDetails = orderService.getOrderStatusDetails(orderId);
            return ResponseEntity.ok(new DefaultResult("Order: " + orderId + " status details: " + statusDetails, false,
                    null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResult> deleteOrder(@PathVariable UUID id) {
        try {
            Order order = orderService.deleteOrder(id);
            return ResponseEntity.ok(new DefaultResult("Order: " + id + " deleted", false, order));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("{id}/status")
    public ResponseEntity<DefaultResult> getOrderStatus(@PathVariable UUID id) {
        try {
            OrderStatus status = orderService.getOrderStatus(id);
            return ResponseEntity.ok(new DefaultResult("Order: " + id + " status is" + status, false, null));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    // // Add item to order
    // @PostMapping("/{orderId}/items")
    // public ResponseEntity<Order> addItemToOrder(
    // @PathVariable UUID orderId,
    // @RequestParam UUID foodItemId,
    // @RequestParam String foodItemName,
    // @RequestParam int quantity,
    // @RequestParam double price) {
    // Order updatedOrder = orderService.addItemToOrder(orderId, foodItemId,
    // foodItemName, quantity, price);
    // return ResponseEntity.ok(updatedOrder);
    // }

    // // Remove item from order
    // @DeleteMapping("/{orderId}/items/{itemId}")
    // public ResponseEntity<Order> removeItemFromOrder(@PathVariable UUID orderId,
    // @PathVariable UUID itemId) {
    // Order updatedOrder = orderService.removeItemFromOrder(orderId, itemId);
    // return ResponseEntity.ok(updatedOrder);
    // }

}