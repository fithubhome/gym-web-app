package com.auth.api;

import com.auth.api.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserContext {
    private static final Map<UUID, User> loggedInUsers = new HashMap<>();

    public static void loginUser(UUID sessionId, User user) {
        loggedInUsers.put(sessionId, user);
    }

    public static void logoutUser(UUID sessionId) {
        loggedInUsers.remove(sessionId);
    }

    public static User getCurrentUser(UUID sessionId) {
        return loggedInUsers.get(sessionId);
    }
}
