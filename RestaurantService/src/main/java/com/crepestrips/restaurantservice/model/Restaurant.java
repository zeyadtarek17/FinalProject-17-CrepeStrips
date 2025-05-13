package com.crepestrips.restaurantservice.model;

import com.crepestrips.fooditemservice.model.FoodItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "restaurants")
public class Restaurant{

    @Id
    private String id;
    private String name;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String location;
    private double rating;
    private boolean isOpen;
    private boolean hasSeating;
    private boolean supportsDelivery;
    private RestaurantType type;
    private boolean isBanned;
    private FoodItem foodItem;
    @DBRef
    private Category category;

    private List<String> foodItemIds = new ArrayList<>();



    public Restaurant(String name, String location, double rating) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        isBanned = false;
    }

    public Restaurant(String id, String name, String location, double rating, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.isOpen = isOpen;
        isBanned = false;
    }

    public Restaurant() {
        isBanned = false;

    }

    public Restaurant(String name, String location, double rating, boolean isOpen, LocalTime openingTime, LocalTime closingTime, Category category) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.isOpen = isOpen;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.category = category;
        isBanned = false;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }

    public boolean isOpen() {
        if (openingTime == null || closingTime == null) {
            return false;
        }
        LocalTime now = LocalTime.now();
        return !now.isBefore(openingTime) && !now.isAfter(closingTime);
    }

    public void setOpen(boolean open) { isOpen = open; }

    public List<String> getFoodItemIds() { return foodItemIds; }

    public void setFoodItemIds(List<String> foodItemIds) { this.foodItemIds = foodItemIds; }

    public LocalTime getOpeningTime() { return openingTime; }

    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

    public LocalTime getClosingTime() { return closingTime; }

    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }



    public String getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public boolean isHasSeating() {
        return hasSeating;
    }

    public void setHasSeating(boolean hasSeating) {
        this.hasSeating = hasSeating;
    }

    public boolean isSupportsDelivery() {
        return supportsDelivery;
    }

    public void setSupportsDelivery(boolean supportsDelivery) {
        this.supportsDelivery = supportsDelivery;
    }

    public RestaurantType getType() {
        return type;
    }

    public void setType(RestaurantType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategoryID(String categoryId) {
        if (this.category == null) {
            this.category = new Category(); // or fetch existing if needed
        }
        this.category.setId(categoryId);
    }
    public boolean isBanned() {
        return isBanned;
    }
    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }
}

