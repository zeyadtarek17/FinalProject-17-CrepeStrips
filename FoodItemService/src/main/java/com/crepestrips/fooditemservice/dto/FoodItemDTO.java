package com.crepestrips.fooditemservice.dto;

public class FoodItemDTO {
    private String id;
    private String name;
    private Double price;
    private String restaurantId;


    public FoodItemDTO() {}

    public FoodItemDTO(String id, String name, Double price, String restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
}
