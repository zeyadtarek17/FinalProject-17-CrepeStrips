
package com.crepestrips.fooditemservice.FoodItemFactory;

import org.springframework.data.annotation.TypeAlias;

import com.crepestrips.fooditemservice.model.FoodItem;

@TypeAlias("main_course")
public class MainCourseItem extends FoodItem {
    private boolean hasSideDish;
    private String descriptionSideDish;
    public MainCourseItem() {
        super();
    }
    public MainCourseItem(FoodItem item) {
        super(item.getName(), item.getDescription(),
                item.getPrice(),
                item.getDiscount(), item.getRating(),
                item.getAvailableStock(), item.getCategory(), item.getRestaurantId(),item.getStatus());
    }


    
    public boolean isHasSideDish() {
        return hasSideDish;
    }
    public void setHasSideDish(boolean hasSideDish) {
        this.hasSideDish = hasSideDish;
    }
    public String getDescriptionSideDish() {
        return descriptionSideDish;
    }
    public void setDescriptionSideDish(String descriptionSideDish) {
        this.descriptionSideDish = descriptionSideDish;
    }
    
}
