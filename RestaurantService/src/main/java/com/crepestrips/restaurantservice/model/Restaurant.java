package com.crepestrips.restaurantservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;
    private String location;
    private double rating;
    private boolean isOpen;

    private List<String> foodItemIds = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String name, String location, double rating, boolean isOpen) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.isOpen = isOpen;
    }

    public Restaurant(String id, String name, String location, double rating, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.isOpen = isOpen;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }

    public boolean isOpen() { return isOpen; }

    public void setOpen(boolean open) { isOpen = open; }

    public List<String> getFoodItemIds() { return foodItemIds; }

    public void setFoodItemIds(List<String> foodItemIds) { this.foodItemIds = foodItemIds; }

}
