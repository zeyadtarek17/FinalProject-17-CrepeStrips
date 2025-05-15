package com.crepestrips.userservice.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
public class Cart {


    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private List<String> items;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}