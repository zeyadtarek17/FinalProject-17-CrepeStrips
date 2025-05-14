package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;

import org.springframework.stereotype.Component;

// @Component("TAKEAWAY")
public class TakeawayRestaurant extends Restaurant {
    private boolean selfService; // unique attribute

    public boolean isSelfService() { return selfService; }
    public void setSelfService(boolean selfService) { this.selfService = selfService; }

    public TakeawayRestaurant(Restaurant restaurant, boolean selfService) {
        super(restaurant.getName(), restaurant.getLocation(), restaurant.isOpen(), restaurant.getOpeningTime(), restaurant.getClosingTime(), restaurant.getCategory());
        this.selfService = selfService; 
    }
}