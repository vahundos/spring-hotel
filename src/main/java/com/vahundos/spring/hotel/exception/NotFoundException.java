package com.vahundos.spring.hotel.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(long id) {
        super("Can't find entity with id = " + id);
    }
}
