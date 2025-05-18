package com.crepestrips.orderservice.model;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
    @Column(columnDefinition = "uuid") // Explicitly define DB column type
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference // This side will NOT be serialized when serializing OrderItem from Order
    private Order order;
    private String foodItemId;
    private String foodItemName;
    private int quantity;
    private double pricePerUnit;
    private double subTotal;

    public OrderItem(String foodItemId, String foodItemName, int quantity, double pricePerUnit) {
        this.id = UUID.randomUUID(); 
        this.foodItemId = foodItemId;
        this.foodItemName = foodItemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subTotal = quantity * pricePerUnit;
    }
}