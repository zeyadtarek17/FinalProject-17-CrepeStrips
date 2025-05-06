
package com.crepestrips.fooditemservice.FoodItemFactory;

import com.crepestrips.fooditemservice.model.FoodItem;

public class DessertItem extends FoodItem {
    public DessertItem(FoodItem item) {
        super(item.getName(), item.getDescription(), item.getPrice(), 
              item.getDiscount(),
              item.getRating(), item.getAvailableStock(), 
              item.getCategory(), item.getRestaurantId());
    }
}
