package com.crepestrips.adminservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BanRestaurantRequest {
    @NotNull
    private String restaurantId;
    private String reason;
}