package com.petruho.spring.project.marketplace.error;

public class EmailNotFoundException extends UserServiceException {

    public EmailNotFoundException(String message) {
        super(message);
    }
}
