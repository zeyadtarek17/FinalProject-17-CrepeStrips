package com.crepestrips.adminservice.dto;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String jwtToken) {
        this.token = jwtToken;
    }
}