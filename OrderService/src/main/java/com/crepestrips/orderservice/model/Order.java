package com.crepestrips.orderservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @Column(columnDefinition = "uuid") // Explicitly define DB column type
    private UUID id;

    @Column(columnDefinition = "uuid")
    private UUID userId;

    private String restaurantId;

    @Column(columnDefinition = "uuid")
    private UUID cartId;
    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderPriority priority;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // This side will be serialized
    private List<OrderItem> orderItems = new ArrayList<>();

    private double totalAmount;

    public Order(UUID userId, String restaurantId, UUID cartId) {
        this.id = UUID.randomUUID(); // Generate ID on creation
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.cartId = cartId;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
        this.priority = OrderPriority.NORMAL;
        this.totalAmount = 0.0;
        this.orderItems = new ArrayList<>();
    }

    // Helper method to add an item and update total amount
    public void addOrderItem(OrderItem item) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(item);
        item.setOrder(this);
        calculateTotalAmount();
    }

    public void removeOrderItem(OrderItem item) {
        if (this.orderItems != null) {
            this.orderItems.remove(item);
            item.setOrder(null);
            calculateTotalAmount();
        }
    }

    // Helper method to calculate total amount
    private void calculateTotalAmount() {
        this.totalAmount = 0.0;
        for (OrderItem item : this.orderItems) {
            this.totalAmount += item.getSubTotal();
        }

    }

}