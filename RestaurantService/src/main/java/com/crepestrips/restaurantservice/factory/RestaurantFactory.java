package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestaurantFactory {

    public Restaurant createRestaurant(String type) {
        switch (type.toUpperCase()) {
            case "DINE_IN":
                DineInRestaurant dineIn = new DineInRestaurant();
                dineIn.setHasSeating(true);
                dineIn.setSupportsDelivery(false);
                dineIn.setType(RestaurantType.DINE_IN);
                dineIn.setTableCount(20); // unique config
                return dineIn;

            case "DELIVERY":
                DeliveryRestaurant delivery = new DeliveryRestaurant();
                delivery.setHasSeating(false);
                delivery.setSupportsDelivery(true);
                delivery.setType(RestaurantType.DELIVERY);
                delivery.setDeliveryZone("Zone A"); // unique config
                return delivery;

            case "TAKEAWAY":
                TakeawayRestaurant takeaway = new TakeawayRestaurant();
                takeaway.setHasSeating(false);
                takeaway.setSupportsDelivery(false);
                takeaway.setType(RestaurantType.TAKEAWAY);
                takeaway.setSelfService(true); // unique config
                return takeaway;

            default:
                throw new IllegalArgumentException("Unknown restaurant type: " + type);
        }
    }
}

