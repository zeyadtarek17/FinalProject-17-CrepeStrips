package com.crepestrips.userservice.dto;

import java.util.UUID;

public class AuthRequest {

    private String username;
    private String password;
    private UUID id;


    // Constructor
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}