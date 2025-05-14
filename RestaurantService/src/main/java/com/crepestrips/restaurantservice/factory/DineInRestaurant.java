package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;

import org.springframework.stereotype.Component;

// @Component("DINE_IN")
public class DineInRestaurant extends Restaurant {
    private int tableCount;

    public int getTableCount() { return tableCount; }
    public void setTableCount(int tableCount) { this.tableCount = tableCount; }

     public DineInRestaurant(Restaurant restaurant, int tableCount) {
    
        super(restaurant.getName(), restaurant.getLocation(), restaurant.isOpen(), restaurant.getOpeningTime(), restaurant.getClosingTime(), restaurant.getCategory());
        this.tableCount = tableCount;
    }
}
