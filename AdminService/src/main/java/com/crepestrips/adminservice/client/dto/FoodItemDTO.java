package com.crepestrips.adminservice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor; // Good practice for DTOs

@Data
@NoArgsConstructor // For Jackson deserialization
@JsonIgnoreProperties(ignoreUnknown = true) // Good practice: Ignores fields in JSON not present in DTO
public class FoodItemDTO {

    @JsonProperty("id") // Explicitly map, though name matches if case is same
    private String id;      // Changed from Long to String

    @JsonProperty("name")
    private String name;

    @JsonProperty("restaurantId")
    private String restaurantId; // Changed from Long to String

    @JsonProperty("status")
    private String status;       // To capture the status string like "UNSUSPENDED"

    // Other fields from JSON you might need in admin-service (optional)
    // @JsonProperty("description")
    // private String description;
    //
    // @JsonProperty("price")
    // private double price;
    //
    // @JsonProperty("availableStock")
    // private int availableStock;
    //
    // @JsonProperty("category")
    // private String category;


    /**
     * Derives the active status based on the 'status' field.
     * This method is what your SuspendFoodItemCommand will call (item.isActive()).
     * Jackson will NOT use this for deserialization of a field named 'active'
     * unless you use @JsonGetter("active"), but we don't have an 'active' field directly from JSON.
     */
    public boolean isActive() {
        // Adjust the logic based on all possible status strings from food-item-service
        return "UNSUSPENDED".equalsIgnoreCase(this.status) || "ACTIVE".equalsIgnoreCase(this.status); // Or whatever signifies active
    }

    // @Data from Lombok will generate:
    // - getters for id, name, restaurantId, status
    // - setters for id, name, restaurantId, status
    // - toString(), equals(), hashCode()
    //
    // You do NOT need to explicitly define a getter for 'active' like 'getActive()'
    // because 'active' is not a field. 'isActive()' is a derived property method.
    // Your command class will call item.isActive() and it will execute the logic above.
}