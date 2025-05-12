package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

@Component("DINE_IN")
public class DineInRestaurant implements RestaurantTypeStrategy {
    @Override
    public void configure(Restaurant restaurant) {
        restaurant.setHasSeating(true);
        restaurant.setSupportsDelivery(false);
    }
}