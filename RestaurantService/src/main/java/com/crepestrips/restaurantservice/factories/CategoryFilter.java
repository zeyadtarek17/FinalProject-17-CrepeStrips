package com.crepestrips.restaurantservice.factories;

import com.crepestrips.restaurantservice.model.Restaurant;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilter implements FilterStrategy {

    @Override
    public List<Restaurant> filter(List<Restaurant> restaurants, Object criteria) {
        String categoryId = criteria.toString();
        return restaurants.stream()
                .filter(r -> r.getCategoryIds().contains(categoryId))
                .collect(Collectors.toList());
    }
}
