package com.crepestrips.orderservice.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.crepestrips.orderservice.dto.FoodItemResponse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    private UUID id;
    private UUID userId;
    private UUID restaurantId;
    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderPriority priority;

    private List<FoodItemResponse> orderItems = new ArrayList<>();

    private double totalAmount;

    public Order(UUID userId, UUID restaurantId, List<FoodItemResponse> orderItems) {
        this.id = UUID.randomUUID(); // Generate ID on creation
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.priority = OrderPriority.NORMAL;
        this.totalAmount = 0.0;
        this.orderItems = orderItems;
    }

    // Helper method to add an item and update total amount
    public void addOrderItem(FoodItemResponse item) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(item);
        calculateTotalAmount();
    }

    public void removeOrderItem(FoodItemResponse item) {
        if (this.orderItems != null) {
            this.orderItems.remove(item);
            calculateTotalAmount();
        }
    }

    // Helper method to calculate total amount
    public void calculateTotalAmount() {
        this.totalAmount = 0.0;
        for (FoodItemResponse item : this.orderItems) {
            this.totalAmount += item.getSubTotal();
        }

    }

}