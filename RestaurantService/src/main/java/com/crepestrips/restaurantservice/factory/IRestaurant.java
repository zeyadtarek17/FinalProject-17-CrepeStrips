package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;

public interface IRestaurant {
    String getName();
    String getLocation();
    boolean isOpen();
    RestaurantType getType();
}

