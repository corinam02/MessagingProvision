package com.example.messageprovisioning.provisioning.model.exception;

public class NoNumberFoundException extends RuntimeException {
    public NoNumberFoundException(String areaCode) {

        super("There are no phone numbers available in this code area" +areaCode);
    }
}
