package com.petruho.spring.project.marketplace.error;

public class NotEnoughElementsOrderException extends OrderException {

    public NotEnoughElementsOrderException(String message) {
        super(message);
    }
}
