package com.gym_app.api.exceptions;

import java.util.UUID;

public class UserNotFoundExceptionToDeleteException extends Exception {
    public UserNotFoundExceptionToDeleteException(UUID userId) {
        super("UserEntity with ID " + userId + " not found to delete.");
    }
}
