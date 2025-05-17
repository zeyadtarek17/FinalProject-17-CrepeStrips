package com.crepestrips.restaurantservice.strategy;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RestaurantFilterContext {
    private final Map<String, FilterStrategy> strategies;

    public RestaurantFilterContext(Map<String, FilterStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<Restaurant> applyFilter(String filterType, List<Restaurant> restaurants, String criteria) {
        FilterStrategy strategy = strategies.get(filterType);
        if (strategy == null) throw new IllegalArgumentException("Invalid filter type");
        System.out.println("Applying filter: " + filterType);
        System.out.println(strategy.filter(restaurants, criteria));
        return strategy.filter(restaurants, criteria);
    }
}

