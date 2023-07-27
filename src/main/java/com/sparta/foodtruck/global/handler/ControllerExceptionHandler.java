package com.sparta.foodtruck.global.handler;

import com.sparta.foodtruck.global.dto.ErrorResponseDto;
import com.sparta.foodtruck.global.exception.CustomStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomStatusException.class)
    public ResponseEntity<ErrorResponseDto> CustomStatusExceptionHandler(CustomStatusException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getStatus().value(), exception.getMessage());
        return ResponseEntity.status(exception.getStatus().value()).body(errorResponseDto);
    }
}
