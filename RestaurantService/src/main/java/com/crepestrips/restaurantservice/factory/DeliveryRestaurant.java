package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

@Component("DELIVERY")
public class DeliveryRestaurant extends Restaurant {
    private String deliveryZone; // unique attribute

    public String getDeliveryZone() { return deliveryZone; }
    public void setDeliveryZone(String deliveryZone) { this.deliveryZone = deliveryZone; }
}