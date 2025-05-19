package com.crepestrips.adminservice.client.dto;// In admin-service: com.crepestrips.adminservice.client.dto.RestaurantDTO.java

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime; // If you want to deserialize opening/closingTime to LocalTime
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Good practice
public class RestaurantDTO {

    private String id;
    private String name;

    // For LocalTime, you might need specific handling if it's just a String "HH:mm:ss"
    // Option 1: Keep as String
    // private String openingTime;
    // private String closingTime;

    // Option 2: Deserialize to LocalTime (requires Jackson JSR310 module)
    // Ensure you have 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310' dependency
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime openingTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime closingTime;

    private String location;
    private boolean hasSeating;
    private boolean supportsDelivery;

    @JsonProperty("type") // Assuming 'type' in JSON maps to 'restaurantType' or similar in DTO
    private String restaurantType; // Or an Enum if you define one

    // "foodItem": null - This field seems problematic if it's meant to be a single item.
    // If it's always null or not used, you can ignore it or map it to an Object.
    // For now, let's assume it's not critical for the ban operation.

    private CategoryDTO category; // Nested DTO

    private List<String> foodItemIds;

    private int tableCount; // Or Integer
    private boolean isOpen;
    private boolean isBanned;

    private String categoryId;

    // Nested DTO for Category
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryDTO { // Made static for nesting
        private String id;
        private String name;
    }
}