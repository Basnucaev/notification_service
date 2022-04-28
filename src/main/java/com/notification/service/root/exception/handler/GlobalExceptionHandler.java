package com.notification.service.root.exception.handler;

import com.notification.service.root.exception.ClientNotFoundException;
import com.notification.service.root.exception.EntityNotSavedException;
import com.notification.service.root.exception.EntityNotUpdatedException;
import com.notification.service.root.exception.MailingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handleClientNotFoundException(ClientNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailingNotFoundException.class)
    public ResponseEntity<String> handleMailingNotFoundException(MailingNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotSavedException.class)
    public ResponseEntity<String> handleEntityNotSavedException(EntityNotSavedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(EntityNotUpdatedException.class)
    public ResponseEntity<String> handleEntityNotUpdatedException(EntityNotUpdatedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>("JSON файл заполнен некорректно", HttpStatus.NOT_ACCEPTABLE);
    }

    //    @ExceptionHandler(.class)
//    public ResponseEntity<String> handle(exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//    }

    //    @ExceptionHandler(.class)
//    public ResponseEntity<String> handle(exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//    }
}
