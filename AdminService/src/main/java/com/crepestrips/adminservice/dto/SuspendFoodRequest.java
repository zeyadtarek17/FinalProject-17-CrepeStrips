package com.crepestrips.adminservice.dto;


import org.jetbrains.annotations.NotNull;
import lombok.Data;

@Data
public class SuspendFoodRequest {
    @NotNull
    private Long foodItemId;
    private String reason;
}