package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

@Component("TAKEAWAY")
public class TakeawayRestaurant extends Restaurant {
    private boolean selfService; // unique attribute

    public boolean isSelfService() { return selfService; }
    public void setSelfService(boolean selfService) { this.selfService = selfService; }
}