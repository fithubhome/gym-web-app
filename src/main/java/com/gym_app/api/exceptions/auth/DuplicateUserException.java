package com.gym_app.api.exceptions.auth;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String email) {
        super("UserEntity with email " + email + " already exists.");
    }
}
