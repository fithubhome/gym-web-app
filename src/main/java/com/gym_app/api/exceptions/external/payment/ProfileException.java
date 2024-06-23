package com.gym_app.api.exceptions.external.payment;

public class ProfileException extends Exception{

    public ProfileException(String className, String field) {
        super("This error is in: " + className + " related to: " + field);
    }
}


