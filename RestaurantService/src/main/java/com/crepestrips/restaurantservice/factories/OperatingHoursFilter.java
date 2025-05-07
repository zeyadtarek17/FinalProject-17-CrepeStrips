package com.crepestrips.restaurantservice.filterFactory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.factories.FilterStrategy;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class OperatingHoursFilter implements FilterStrategy {

    @Override
    public List<Restaurant> filter(List<Restaurant> restaurants, Object criteria) {
        if (!(criteria instanceof LocalTime[])) return restaurants;
        LocalTime[] times = (LocalTime[]) criteria;
        return restaurants.stream()
                .filter(r -> r.getOpeningTime().isBefore(times[0]) && r.getClosingTime().isAfter(times[1]))
                .collect(Collectors.toList());
    }
}
