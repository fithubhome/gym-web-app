package com.auth.api;

import com.auth.api.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserContext {
    private static final Map<String, User> loggedInUsers = new HashMap<>();

    public static void loginUser(String sessionId, User user) {
        loggedInUsers.put(sessionId, user);
    }

    public static void logoutUser(String sessionId) {
        loggedInUsers.remove(sessionId);
    }

    public static User getCurrentUser(String sessionId) {
        return loggedInUsers.get(sessionId);
    }
}
