package com.crepestrips.orderservice.dto; // Note: This DTO might live in a shared library or in OrderService

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private UUID cartId; // The ID of the cart itself
    private UUID userId; // The ID of the user who owns the cart
    private List<String> foodItemIds; // List of food item IDs from the cart

    @Override
    public String toString() {
        return "CartDTO{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", foodItemIds=" + foodItemIds +
                '}';
    }
}