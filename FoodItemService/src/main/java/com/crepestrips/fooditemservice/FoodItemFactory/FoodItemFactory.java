package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

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
}
