package com.crepestrips.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private UUID userId;
    private UUID requestId; // To correlate with the request
    private List<String> foodItemIds;
    private UUID restaurantId;
}