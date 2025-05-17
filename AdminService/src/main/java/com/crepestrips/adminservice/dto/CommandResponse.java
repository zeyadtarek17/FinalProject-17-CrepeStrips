package com.crepestrips.adminservice.dto;


import lombok.Data;

@Data
public class CommandResponse {
    private String status;
    private String message;
    private String commandType;
    public CommandResponse() {}

    // All-args constructor
    public CommandResponse(String status, String message, String commandType) {
        this.status = status;
        this.message = message;
        this.commandType = commandType;
    }

    // Getters and Setters (required without Lombok)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}