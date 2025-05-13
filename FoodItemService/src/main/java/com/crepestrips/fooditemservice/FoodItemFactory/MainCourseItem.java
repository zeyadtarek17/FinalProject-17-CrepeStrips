
package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

public class MainCourseItem extends FoodItem {
    private String description;
    public MainCourseItem() {
        super();
    }
    public MainCourseItem(FoodItem item) {
        super(item.getName(), item.getDescription(),
                item.getPrice(),
                item.getDiscount(), item.getRating(),
                item.getAvailableStock(), item.getCategory(), item.getRestaurantId());
                this.description="250 grams of steak served with 200 grams of rice";
    }


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
