package com.vahundos.spring.hotel.web.handler;

import com.vahundos.spring.hotel.exception.InvalidEntityException;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.web.CommonHttpException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public final ResponseEntity<?> handleNotFoundExceptions() {
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CommonHttpException.class})
    public final ResponseEntity<?> handleResponseStatusException(CommonHttpException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(exception.getBody(), headers, exception.getHttpStatus());
    }

    @ExceptionHandler({InvalidEntityException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
                       HttpMessageNotReadableException.class, HttpMediaTypeException.class})
    public final ResponseEntity<?> handleBadRequestExceptions() {
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<?> handleExceptions(Exception e) {
        return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
