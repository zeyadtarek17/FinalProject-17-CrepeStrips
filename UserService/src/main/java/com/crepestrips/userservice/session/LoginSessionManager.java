package com.crepestrips.userservice.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton to manage logged-in user sessions.
 * Ensures a user can only be logged in once at a time.
 */
public class LoginSessionManager {

    private static final LoginSessionManager instance = new LoginSessionManager();

    // Thread-safe set of currently logged-in usernames
    private final Set<String> loggedInUsers = ConcurrentHashMap.newKeySet();

    private LoginSessionManager() {}

    public static LoginSessionManager getInstance() {
        return instance;
    }

    public boolean isLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    public void login(String username) {
        if (!loggedInUsers.add(username)) {
            throw new IllegalStateException("User is already logged in");
        }
    }

    public void logout(String username) {
        loggedInUsers.remove(username);
    }
}
