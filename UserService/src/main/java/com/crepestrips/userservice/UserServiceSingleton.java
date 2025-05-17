package com.crepestrips.userservice;

import com.crepestrips.userservice.service.UserService;

public class UserServiceSingleton {

    private static UserService instance;

    private UserServiceSingleton() {}

    public static synchronized UserService getInstance(UserService injectedService) {
        if (instance == null) {
            System.out.println("🔁 UserService singleton initialized!");
            instance = injectedService;
        }
        return instance;
    }

    public static UserService getInstance() {
        return instance;
    }
}
