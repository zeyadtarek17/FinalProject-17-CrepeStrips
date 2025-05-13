package com.crepestrips.orderservice.dto;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

public class FoodItemResponse {
    private UUID id;
    private String name;
    private double price;
    private double discount;
    private int availableStock;
    private UUID restaurantId;

    public FoodItemResponse() {
    }

    public FoodItemResponse(UUID id, String name, double price, double discount, int availableStock,
            UUID restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.availableStock = availableStock;
        this.restaurantId = restaurantId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getSubTotal() {
        return (price - discount);
    }

}
