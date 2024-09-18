package com.petruho.spring.project.marketplace.error;

public class EmptyOrderException extends OrderException {

    public EmptyOrderException(String message) {
        super(message);
    }
}
