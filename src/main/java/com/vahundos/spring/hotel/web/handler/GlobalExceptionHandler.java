package com.vahundos.spring.hotel.web.handler;

import com.vahundos.spring.hotel.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public final ResponseEntity<?> handleNotFoundExceptions() {
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public final ResponseEntity<?> handleResponseStatusException(ResponseStatusException exception) {
        if (exception.getStatus() == HttpStatus.OK) {
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }

        return new ResponseEntity<>("", exception.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class,
                       HttpMediaTypeException.class})
    public final ResponseEntity<?> handleBadRequestExceptions() {
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<?> handleExceptions(Exception e) {
        return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
