package com.crepestrips.restaurantservice.dto;

public class FoodItemDTO {
    private String id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private double rating;
    private int availableStock;
    private String category;       
    private String restaurantId;
    private String status;

    private Integer sweetnessLevel;

    private Boolean hasSideDish;
    private String descriptionSideDish;

    public FoodItemDTO() {}

    public FoodItemDTO(String id, String name, String description, double price, double discount,
                       double rating, int availableStock, String category, String restaurantId, String status,
                       Integer sweetnessLevel, Boolean hasSideDish, String descriptionSideDish) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.rating = rating;
        this.availableStock = availableStock;
        this.category = category;
        this.restaurantId = restaurantId;
        this.status = status;
        this.sweetnessLevel = sweetnessLevel;
        this.hasSideDish = hasSideDish;
        this.descriptionSideDish = descriptionSideDish;
    }

    // Standard Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getSweetnessLevel() { return sweetnessLevel; }
    public void setSweetnessLevel(Integer sweetnessLevel) { this.sweetnessLevel = sweetnessLevel; }

    public Boolean getHasSideDish() { return hasSideDish; }
    public void setHasSideDish(Boolean hasSideDish) { this.hasSideDish = hasSideDish; }

    public String getDescriptionSideDish() { return descriptionSideDish; }
    public void setDescriptionSideDish(String descriptionSideDish) { this.descriptionSideDish = descriptionSideDish; }
}
