package com.crepestrips.restaurantservice.factories;

public class FilterFactory {

    public static FilterStrategy getFilter(String type) {
        switch (type) {
            case "category":
                return new CategoryFilter();
            case "hours":
                return new com.crepestrips.restaurantservice.factories.OperatingHoursFilter();
            default:
                throw new IllegalArgumentException("Invalid filter type");
        }
    }
}
