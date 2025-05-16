package com.crepestrips.restaurantservice.model;


import java.util.Map;

public class RestaurantCreation {
    private Restaurant restaurant;
    private Map<String, Object> extras;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }
}
