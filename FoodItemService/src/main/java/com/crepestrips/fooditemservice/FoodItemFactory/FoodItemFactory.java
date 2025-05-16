package com.crepestrips.fooditemservice.FoodItemFactory;

import java.util.Map;

import com.crepestrips.fooditemservice.model.FoodCategory;
import com.crepestrips.fooditemservice.model.FoodItem;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FoodItemFactory {

    public static FoodItem createFoodItem(FoodItem item) {
        if (item.getCategory() == null) {
            return item;
        }

        switch (item.getCategory()) {
            case DESSERT:
                return new DessertItem(item);
            case MAIN_COURSE:
                return new MainCourseItem(item);
            default:
                return item;
        }
    }
    public static FoodItem createFoodItem(FoodCategory category, Map<String, Object> data, ObjectMapper mapper) {
        if (category == null) {
            return mapper.convertValue(data, FoodItem.class);
        }

        return switch (category) {
            case DESSERT -> mapper.convertValue(data, DessertItem.class);
            case MAIN_COURSE -> mapper.convertValue(data, MainCourseItem.class);
            default -> mapper.convertValue(data, FoodItem.class);
        };
    }
}
