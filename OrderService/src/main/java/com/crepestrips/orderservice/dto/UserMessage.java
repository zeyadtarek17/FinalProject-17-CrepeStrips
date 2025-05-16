package com.crepestrips.orderservice.dto;


import java.util.List;
import java.util.UUID;

public class UserMessage {
    private UUID userId;
    private UUID restrauntId;
    private List<FoodItemResponse> foodItems;
    private UUID orderId;
    public UserMessage() {}

    public UserMessage(UUID userId, UUID restrauntId, List<FoodItemResponse> foodItems, UUID orderId) {
        this.userId = userId;
        this.restrauntId = restrauntId;
        this.foodItems = foodItems;
        this.orderId = orderId;
    }

    public UserMessage(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRestrauntId() {
        return restrauntId;
    }

    public void setRestrauntId(UUID restrauntId) {
        this.restrauntId = restrauntId;
    }

    public List<FoodItemResponse> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItemResponse> foodItems) {
        this.foodItems = foodItems;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
