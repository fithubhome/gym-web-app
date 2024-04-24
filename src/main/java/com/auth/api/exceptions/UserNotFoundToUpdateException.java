package com.auth.api.exceptions;

public class UserNotFoundToUpdateException extends Exception {
    public UserNotFoundToUpdateException(int userId) {
        super("User with ID " + userId + " not found to update.");
    }
}
