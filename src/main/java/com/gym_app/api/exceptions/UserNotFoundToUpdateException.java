package com.gym_app.api.exceptions;

import java.util.UUID;

public class UserNotFoundToUpdateException extends Exception {
    public UserNotFoundToUpdateException(UUID userId) {
        super("User with ID " + userId + " not found to update.");
    }
}
