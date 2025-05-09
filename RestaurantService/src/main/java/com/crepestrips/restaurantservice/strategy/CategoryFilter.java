package com.crepestrips.restaurantservice.strategy;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("categoryFilter")
public class CategoryFilter implements FilterStrategy {

    @Override
    public List<Restaurant> filter(List<Restaurant> restaurants, String categoryId) {
        return restaurants.stream()
                .filter(r -> r.getCategoryId().contains(categoryId))
                .collect(Collectors.toList());
    }
}
