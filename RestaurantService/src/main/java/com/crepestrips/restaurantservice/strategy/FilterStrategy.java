package com.crepestrips.restaurantservice.strategy;

import com.crepestrips.restaurantservice.model.Restaurant;
import java.util.List;

public interface FilterStrategy {
    List<Restaurant> filter(List<Restaurant> restaurants, String categoryId);
}
