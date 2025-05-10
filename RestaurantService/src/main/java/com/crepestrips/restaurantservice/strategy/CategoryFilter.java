package com.crepestrips.restaurantservice.strategy;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("categoryFilter")
public class CategoryFilter implements FilterStrategy {

    @Override
    public List<Restaurant> filter(List<Restaurant> restaurants, String categoryName) {
        return restaurants.stream()
                .filter(r -> r.getCategory() != null &&
                        r.getCategory().getName() != null &&
                        r.getCategory().getName().name().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }
}
