package com.crepestrips.userservice.dto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto { 
    private UUID cartId;
    private UUID userId;
    private List<String> foodItemIds;
    
}