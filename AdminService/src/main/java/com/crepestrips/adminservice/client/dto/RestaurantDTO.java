package com.crepestrips.adminservice.client.dto;


import lombok.Data;

@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private boolean banned;
    // Add other fields as needed
}