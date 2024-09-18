package com.petruho.spring.project.marketplace.error;

public abstract class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }
}
