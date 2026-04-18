package com.example.messageprovisioning.provisioning.model.exception;

public class NumberNotFoundException extends RuntimeException{
    public NumberNotFoundException(String numberId){
        super("This phone number with the id" +numberId + "was not found");
    }

}
