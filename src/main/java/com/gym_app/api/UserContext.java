package com.gym_app.api;

import com.gym_app.api.model.UserEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserContext {
    private static final Map<UUID, UserEntity> loggedInUsers = new HashMap<>();

    public static void loginUser(UUID sessionId, UserEntity userEntity) {
        loggedInUsers.put(sessionId, userEntity);
    }

    public static void logoutUser(UUID sessionId) {
        loggedInUsers.remove(sessionId);
    }

    public static UserEntity getCurrentUser(UUID sessionId) {
        return loggedInUsers.get(sessionId);
    }
}
