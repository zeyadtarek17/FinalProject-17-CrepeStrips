package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import com.crepestrips.restaurantservice.model.RestaurantType;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.stereotype.Component;

// @Component("DELIVERY")
@TypeAlias("deliveryrestaurant")
public class DeliveryRestaurant extends Restaurant {
    private String deliveryZone; // unique attribute

    public String getDeliveryZone() { return deliveryZone; }
    public void setDeliveryZone(String deliveryZone) { this.deliveryZone = deliveryZone; }

    public DeliveryRestaurant() {
        super();
    }

    public DeliveryRestaurant(Restaurant restaurant, String deliveryZone) {
        super(restaurant.getName(), restaurant.getLocation(), restaurant.isOpen(), restaurant.getOpeningTime(), restaurant.getClosingTime(), restaurant.getCategory());
        this.deliveryZone = deliveryZone;
    }
}