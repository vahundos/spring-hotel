package com.vahundos.spring.hotel.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonHttpException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String body;

    public CommonHttpException(HttpStatus httpStatus, String body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }
}
