package com.crepestrips.adminservice.client.dto;

import lombok.Data;

@Data
public class FoodItemDTO {
    private Long id;
    private String name;
    private boolean active;
    private Long restaurantId;
    // Add other fields as needed
}