
package com.crepestrips.fooditemservice.FoodItemFactory;

import org.springframework.data.annotation.TypeAlias;

import com.crepestrips.fooditemservice.model.FoodItem;

@TypeAlias("dessert")
public class DessertItem extends FoodItem {
    private int sweetnessLevel;
    public DessertItem() {super();}
    public DessertItem(FoodItem item) {
        super(item.getName(), item.getDescription(), item.getPrice(), 
              item.getDiscount(),
              item.getRating(), item.getAvailableStock(), 
              item.getCategory(), item.getRestaurantId(),item.getStatus());
    }
    public int getSweetnessLevel() {
        return sweetnessLevel;
    }
    public void setSweetnessLevel(int sweetnessLevel) {
        this.sweetnessLevel = sweetnessLevel;
    }
    
}
