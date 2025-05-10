package com.crepestrips.userservice.dto;


public class AuthResponse {

    private String jwtToken;

    // Constructor
    public AuthResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }


}

