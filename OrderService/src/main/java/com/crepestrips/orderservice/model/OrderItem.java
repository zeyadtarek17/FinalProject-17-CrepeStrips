package com.crepestrips.orderservice.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private UUID foodItemId;
    private String foodItemName;
    private int quantity;
    private double pricePerUnit;
    private double subTotal;

    public OrderItem(UUID foodItemId, String foodItemName, int quantity, double pricePerUnit) {
        this.id = UUID.randomUUID(); 
        this.foodItemId = foodItemId;
        this.foodItemName = foodItemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subTotal = quantity * pricePerUnit;
    }
}
