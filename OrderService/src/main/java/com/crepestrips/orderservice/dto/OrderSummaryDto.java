package com.crepestrips.orderservice.dto;

import com.crepestrips.orderservice.model.OrderPriority;
import com.crepestrips.orderservice.model.OrderStatus;
import lombok.Data; // Or add getters/setters manually
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data 
@NoArgsConstructor
public class OrderSummaryDto {

    private UUID id; // Matches Order.id
    private UUID userId; // Matches Order.userId
    private LocalDateTime orderTime; // Matches Order.orderTime
    private OrderStatus status; // Matches Order.status
    private OrderPriority priority; // Matches Order.priority
    private double totalAmount; // Matches Order.totalAmount

    // We are intentionally omitting orderItems, cartId, restaurantId for a"summary"
}