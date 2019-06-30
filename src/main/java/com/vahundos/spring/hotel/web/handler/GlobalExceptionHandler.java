package com.vahundos.spring.hotel.web.handler;

import com.vahundos.spring.hotel.exception.InvalidEntityException;
import com.vahundos.spring.hotel.exception.NotFoundException;
import com.vahundos.spring.hotel.web.CommonHttpException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GlobalExceptionHandler {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Exception occurred - {}, {}";

    @ExceptionHandler({NotFoundException.class})
    public final ResponseEntity<?> handleNotFoundExceptions(Exception e) {
        log.error(EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e.getCause());

        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CommonHttpException.class})
    public final ResponseEntity<?> handleResponseStatusException(CommonHttpException e) {
        log.error(EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e.getCause());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(e.getBody(), headers, e.getHttpStatus());
    }

    @ExceptionHandler({InvalidEntityException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
                       HttpMessageNotReadableException.class, HttpMediaTypeException.class})
    public final ResponseEntity<?> handleBadRequestExceptions(Exception e) {
        log.error(EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e.getCause());
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<?> handleExceptions(Exception e) {
        log.error(EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e.getCause());
        return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
