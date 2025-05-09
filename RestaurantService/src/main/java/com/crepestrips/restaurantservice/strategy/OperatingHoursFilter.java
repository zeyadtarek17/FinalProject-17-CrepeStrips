package com.crepestrips.restaurantservice.strategy;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component("hoursFilter")
public class OperatingHoursFilter implements FilterStrategy {

    @Override
    public List<Restaurant> filter(List<Restaurant> restaurants, String currentTimeStr) {
        LocalTime currentTime = LocalTime.parse(currentTimeStr);
        return restaurants.stream().filter(restaurant -> currentTime.isAfter(restaurant.getOpeningTime())&&currentTime.isBefore(restaurant.getClosingTime())).collect(Collectors.toList());
    }
}
