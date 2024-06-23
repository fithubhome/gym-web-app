package com.gym_app.api.exceptions.external.membership;

public class MembershipSelectionException extends Exception {
    public MembershipSelectionException(String className, String dataInput){
        super("The input: " + dataInput + " based on the class "+ className + " does not have a valid format");
    }

}
