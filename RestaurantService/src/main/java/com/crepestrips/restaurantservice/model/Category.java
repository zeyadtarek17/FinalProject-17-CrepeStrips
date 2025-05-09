package com.crepestrips.restaurantservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    private RestaurantCategory name;

    public Category() {}

    public Category(RestaurantCategory name) {
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RestaurantCategory getName() {
        return name;
    }

    public void setName(RestaurantCategory name) {
        this.name = name;
    }



}