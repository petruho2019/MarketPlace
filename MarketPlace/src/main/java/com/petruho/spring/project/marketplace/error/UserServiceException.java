package com.petruho.spring.project.marketplace.error;

public abstract class UserServiceException  extends RuntimeException{

    public UserServiceException(String message) {
        super(message);
    }
}
