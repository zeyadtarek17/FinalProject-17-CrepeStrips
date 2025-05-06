package com.crepestrips.fooditemservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "food_items")
public class FoodItem implements IFoodItem {

    @Id
    private String id;

    private String name;
    private String description;
    private double price;
    private double discount;       
    private double rating;         
    private int availableStock;
    private FoodCategory category;
    private String restaurantId;   

    public FoodItem() {}

    public FoodItem(String name, String description, double price, double discount, double rating, int availableStock, FoodCategory category, String restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.rating = rating;
        this.availableStock = availableStock;
        this.category = category;
        this.restaurantId = restaurantId;
    }

    public FoodItem(String id, String name, String description, double price, double discount, double rating, int availableStock, FoodCategory category, String restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.rating = rating;
        this.availableStock = availableStock;
        this.category = category;
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
