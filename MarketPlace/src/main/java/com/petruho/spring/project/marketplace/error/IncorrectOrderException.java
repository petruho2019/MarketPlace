package com.petruho.spring.project.marketplace.error;

public class IncorrectOrderException extends OrderException {

    public IncorrectOrderException(String message) {
        super(message);
    }
}
