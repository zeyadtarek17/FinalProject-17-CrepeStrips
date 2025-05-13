package com.crepestrips.userservice.dto;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

public class FoodItemResponse {
    private String id;
    private String name;
    private double price;
    private double discount;
    private int availableStock;
    private String restaurantId;

    public FoodItemResponse() {
    }

    public FoodItemResponse(String id, String name, double price, double discount, int availableStock, String restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.availableStock = availableStock;
        this.restaurantId = restaurantId;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
    





    
}
