package com.crepestrips.adminservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")

public class Admin {

    @Id
    private String id;

    private String username;

    private String password;

    private String email;


    // Constructors
    public Admin() {}

    public Admin(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

}