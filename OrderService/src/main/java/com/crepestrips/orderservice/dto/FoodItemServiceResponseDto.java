package com.crepestrips.orderservice.dto;
import lombok.Data; // etc.
@Data
public class FoodItemServiceResponseDto {
    private String message;
    private boolean error;
    private FoodItemDto result; // Your existing FoodItemDto

    // Getters, setters, constructors
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public FoodItemDto getResult() { return result; }
    public void setResult(FoodItemDto result) { this.result = result; }
}