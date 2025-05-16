package com.crepestrips.restaurantservice.dto;

import org.springframework.core.annotation.Order;

import java.util.List;

public class RestaurantOrderHistoryResponse {
    private String restaurantId;
    private List<Order> orders;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // ✅ Setter
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
