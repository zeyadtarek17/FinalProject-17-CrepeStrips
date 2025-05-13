
package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

public class DessertItem extends FoodItem {
    private int sweetnessLevel;
    public DessertItem() {super();}
    public DessertItem(FoodItem item) {
        super(item.getName(), item.getDescription(), item.getPrice(), 
              item.getDiscount(),
              item.getRating(), item.getAvailableStock(), 
              item.getCategory(), item.getRestaurantId(),item.getStatus());
        this.sweetnessLevel = 4;
    }
}
