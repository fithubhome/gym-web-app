package com.gym_app.api.exceptions.auth;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
