package com.crepestrips.restaurantservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String location;
    private double rating;

    private List<String> foodItemIds = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String name, String location, double rating) {
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    public Restaurant(String id, String name, String location, double rating, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }

    public List<String> getFoodItemIds() { return foodItemIds; }

    public void setFoodItemIds(List<String> foodItemIds) { this.foodItemIds = foodItemIds; }

    public LocalTime getOpeningTime() { return openingTime; }

    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

    public LocalTime getClosingTime() { return closingTime; }

    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }

}
