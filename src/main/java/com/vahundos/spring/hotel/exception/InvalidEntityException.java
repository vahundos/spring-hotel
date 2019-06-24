package com.vahundos.spring.hotel.exception;

public class InvalidEntityException extends RuntimeException {

    public InvalidEntityException(String message) {
        super(message);
    }
}
