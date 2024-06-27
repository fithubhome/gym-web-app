package com.gym_app.api.exceptions.bodystats;

public class BodystatsNotFoundException extends RuntimeException {
    public BodystatsNotFoundException(String message) {
        super(message);
    }
}
