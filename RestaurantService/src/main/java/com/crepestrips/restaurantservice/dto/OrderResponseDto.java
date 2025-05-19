package com.crepestrips.restaurantservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private String id;
    private String userId;
    private String restaurantId;
    private String cartId;
    private LocalDateTime orderTime;
    private String status;
    private String priority;
    private List<OrderItemDTO> orderItems;
    private double totalAmount;

    @Data
    public static class OrderItemDTO {
        private String foodItemId;
        private String foodItemName;
        private int quantity;
        private double pricePerUnit;
        private double subTotal;
    }
}
