package com.crepestrips.fooditemservice.model;

public interface IFoodItem {
    String getName();
    double getPrice();
    double getDiscount();
    String getId();
    String getDescription();
    double getRating();
    int getAvailableStock();
    FoodCategory getCategory();
    String getRestaurantId();
}
