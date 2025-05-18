package com.crepestrips.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
    private UUID userId;
    private UUID requestId; // To correlate request and response
}