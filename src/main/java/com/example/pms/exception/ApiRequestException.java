package com.example.pms.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends RuntimeException {

    private final String errorMessage;
    private final HttpStatus httpStatus;

    public ApiRequestException(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
