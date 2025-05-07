package com.crepestrips.restaurantservice.strategy;

public class FilterFactory {

    public static FilterStrategy getFilter(String type) {
        switch (type) {
            case "category":
                return new CategoryFilter();
            case "hours":
                return new com.crepestrips.restaurantservice.strategy.OperatingHoursFilter();
            default:
                throw new IllegalArgumentException("Invalid filter type");
        }
    }
}
