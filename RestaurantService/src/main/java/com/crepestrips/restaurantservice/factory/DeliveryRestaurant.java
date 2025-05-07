package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

@Component("DELIVERY")
public class DeliveryRestaurant implements RestaurantTypeStrategy {
    @Override
    public void configure(Restaurant restaurant) {
        restaurant.setHasSeating(false);
        restaurant.setSupportsDelivery(true);
    }
}