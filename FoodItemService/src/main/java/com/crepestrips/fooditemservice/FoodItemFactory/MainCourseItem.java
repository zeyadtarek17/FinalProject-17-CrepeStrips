
package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

public class MainCourseItem extends FoodItem {
    public MainCourseItem() {
        super();}
    public MainCourseItem(FoodItem item) {
        super(item.getName(), item.getDescription(),
                item.getPrice(),
                item.getDiscount(), item.getRating(),
                item.getAvailableStock(), item.getCategory(), item.getRestaurantId());
    }
}
