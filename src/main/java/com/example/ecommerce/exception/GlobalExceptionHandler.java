package com.example.ecommerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException exception) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", exception.getErrorMessage());
        errors.put("httpstatus", exception.getHttpStatus() != null ? exception.getHttpStatus() : HttpStatus.BAD_REQUEST);
        errors.put("timestamp", new Date().toString());
        return ResponseEntity.status(
                exception.getHttpStatus() != null ? exception.getHttpStatus() : HttpStatus.BAD_REQUEST
        ).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        errors.put("httpstaus", HttpStatus.INTERNAL_SERVER_ERROR);
        errors.put("timestamp", new Date().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error","Validation Error(s)");
        errors.put("httpstatus",HttpStatus.BAD_REQUEST);
        errors.put("timestamp", new Date().toString());
        ex.getBindingResult().getAllErrors().forEach(err -> errors.put(
                ((FieldError)err).getField(),
                err.getDefaultMessage()));


        return ResponseEntity.badRequest().body(errors);
    }


}
