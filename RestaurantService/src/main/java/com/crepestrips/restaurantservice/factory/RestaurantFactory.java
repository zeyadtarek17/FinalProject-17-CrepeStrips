package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

public class RestaurantFactory {

    public static Restaurant createRestaurant(RestaurantType type, Map<String, Object> data, ObjectMapper mapper) {
        if (type == null) return mapper.convertValue(data, Restaurant.class);

        return switch (type) {
            case DELIVERY -> mapper.convertValue(data, DeliveryRestaurant.class);
            case DINE_IN -> mapper.convertValue(data, DineInRestaurant.class);
            case TAKEAWAY -> mapper.convertValue(data, TakeawayRestaurant.class);
            default -> mapper.convertValue(data, Restaurant.class);
        };
    }

    public static Restaurant createRestaurant(Restaurant restaurant) {
        return switch (restaurant.getType()) {
            case DELIVERY -> new DeliveryRestaurant(restaurant, ((DeliveryRestaurant) restaurant).getDeliveryZone());
            case DINE_IN -> new DineInRestaurant(restaurant, ((DineInRestaurant) restaurant).getTableCount());
            case TAKEAWAY -> new TakeawayRestaurant(restaurant, ((TakeawayRestaurant) restaurant).isSelfService());
            default -> restaurant;
        };
    }
}

