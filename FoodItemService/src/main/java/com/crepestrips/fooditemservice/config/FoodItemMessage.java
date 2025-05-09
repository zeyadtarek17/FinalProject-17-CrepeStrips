package com.crepestrips.fooditemservice.config;

import com.crepestrips.fooditemservice.dto.FoodItemDTO;

public class FoodItemMessage {
    private String action;
    private String foodItemId;
    private FoodItemDTO payload;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }

    public FoodItemDTO getPayload() {
        return payload;
    }

    public void setPayload(FoodItemDTO payload) {
        this.payload = payload;
    }
}
