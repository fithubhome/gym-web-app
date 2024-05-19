package com.gym_app.api.exceptions;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String email) {
        super("User with email " + email + " already exists.");
    }
}
