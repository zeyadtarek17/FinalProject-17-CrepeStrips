package com.crepestrips.restaurantservice.factory;

import com.crepestrips.restaurantservice.model.Restaurant;
import org.springframework.stereotype.Component;

@Component("DINE_IN")
public class DineInRestaurant extends Restaurant {
    private int tableCount; // unique attribute

    public int getTableCount() { return tableCount; }
    public void setTableCount(int tableCount) { this.tableCount = tableCount; }
}
