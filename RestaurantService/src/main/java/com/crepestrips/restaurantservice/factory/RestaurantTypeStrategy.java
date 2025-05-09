package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;

public interface RestaurantTypeStrategy {
    void configure(Restaurant restaurant);
}

