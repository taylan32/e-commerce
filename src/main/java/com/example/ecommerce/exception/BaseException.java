package com.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseException extends RuntimeException {

    private HttpStatus httpStatus;
    private String errorMessage;

}
