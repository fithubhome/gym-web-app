package com.gym_app.api.exceptions.external.payment;

public class PaymentSelectionException extends Exception {
    public PaymentSelectionException(String className, String dataInput){
        super("The input: " + dataInput + " based on the class "+ className + " does not have a valid format");
    }

}
