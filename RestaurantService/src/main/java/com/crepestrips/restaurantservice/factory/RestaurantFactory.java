package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestaurantFactory {
    private final Map<String,RestaurantTypeStrategy> strategies;

    @Autowired
    public RestaurantFactory(Map<String, RestaurantTypeStrategy> strategies) {
        this.strategies = strategies;
    }

    public Restaurant createRestaurant(Restaurant base, String type){
        RestaurantTypeStrategy strategy = strategies.get(type.toUpperCase());
        if (strategy == null) throw new IllegalArgumentException("Unknown type: " + type);
        strategy.configure(base);
        base.setType(RestaurantType.valueOf(type.toUpperCase()));
        return base;
    }


}
