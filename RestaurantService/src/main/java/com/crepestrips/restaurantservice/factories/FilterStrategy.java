package com.crepestrips.restaurantservice.factories;

import com.crepestrips.restaurantservice.model.Restaurant;
import java.util.List;

public interface FilterStrategy {
    List<Restaurant> filter(List<Restaurant> restaurants, Object criteria);
}
