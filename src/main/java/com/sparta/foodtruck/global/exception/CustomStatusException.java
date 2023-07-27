package com.sparta.foodtruck.global.exception;

import org.springframework.http.HttpStatus;

public class CustomStatusException extends RuntimeException{
    HttpStatus status;

    private CustomStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private CustomStatusException(CustomStatusExceptionBuilder builder) {
        super(builder.message);
        this.status = builder.status;
    }

    public static CustomStatusExceptionBuilder builder(String message){
        return new CustomStatusExceptionBuilder(message);
    }

    public static class CustomStatusExceptionBuilder{
        String message;
        HttpStatus status;

        public CustomStatusExceptionBuilder(String message) {
            this.message = message;
            this.status = HttpStatus.BAD_REQUEST;
        }

        public CustomStatusExceptionBuilder status(int status){
            this.status = HttpStatus.valueOf(status);
            return this;
        }
        public CustomStatusExceptionBuilder status(HttpStatus status){
            this.status = status;
            return this;
        }

        public CustomStatusException build(){
            return new CustomStatusException(this);
        }
    }
}
