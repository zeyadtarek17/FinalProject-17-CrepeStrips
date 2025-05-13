
package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

public class MainCourseItem extends FoodItem {
    private String descriptionMaincourse;
    public MainCourseItem() {
        super();
    }
    public MainCourseItem(FoodItem item) {
        super(item.getName(), item.getDescription(),
                item.getPrice(),
                item.getDiscount(), item.getRating(),
                item.getAvailableStock(), item.getCategory(), item.getRestaurantId(),item.getStatus());
                this.descriptionMaincourse="250 grams of steak served with 200 grams of rice";
    }


    public String getDescriptionMaincourse() {
        return descriptionMaincourse;
    }

    public void setDescriptionMaincourse(String description) {
        this.descriptionMaincourse = description;
    }
}
