package com.crepestrips.adminservice.dto;


import org.jetbrains.annotations.NotNull;
import lombok.Data;

@Data
public class SuspendFoodRequest {
    @NotNull
    private String foodItemId;
    private String reason;

    public @NotNull String getFoodItemId() {
        return foodItemId;
    }


}