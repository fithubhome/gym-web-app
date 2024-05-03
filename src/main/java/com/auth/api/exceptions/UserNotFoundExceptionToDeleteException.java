package com.auth.api.exceptions;

import java.util.UUID;

public class UserNotFoundExceptionToDeleteException extends Exception {
    public UserNotFoundExceptionToDeleteException(UUID userId) {
        super("User with ID " + userId + " not found to delete.");
    }
}

