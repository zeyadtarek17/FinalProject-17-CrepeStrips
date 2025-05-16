package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestaurantFactory {

    public Restaurant createRestaurant(Restaurant restaurant, Map<String, Object> extras) {
        switch (restaurant.getType()) {
            case DINE_IN:
                int tableCount = (int) extras.getOrDefault("tableCount", 0);
                DineInRestaurant dineIn = new DineInRestaurant(restaurant, tableCount);
                // dineIn.setHasSeating(true);
                // dineIn.setSupportsDelivery(false);
                 dineIn.setType(RestaurantType.DINE_IN);
                // dineIn.setTableCount(20); // unique config
                return dineIn;

            case DELIVERY:
                String zone = (String) extras.getOrDefault("deliveryZone", "Zone A");
                DeliveryRestaurant delivery = new DeliveryRestaurant(restaurant, zone);
                // delivery.setHasSeating(false);
                // delivery.setSupportsDelivery(true);
                 delivery.setType(RestaurantType.DELIVERY);
                // delivery.setDeliveryZone("Zone A"); // unique config
                return delivery;

            case TAKEAWAY:
                boolean selfService = (boolean) extras.getOrDefault("selfService", false);
                System.out.println("Self Service: " + selfService);
                TakeawayRestaurant takeaway = new TakeawayRestaurant(restaurant, selfService);
                // takeaway.setHasSeating(false);
                // takeaway.setSupportsDelivery(false);
                 takeaway.setType(RestaurantType.TAKEAWAY);
                // takeaway.setSelfService(true); // unique config
                return takeaway;

            default:
                throw new IllegalArgumentException("Unknown restaurant type: " + restaurant.getType());
        }
    }
}

