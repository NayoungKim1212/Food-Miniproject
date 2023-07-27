package com.sparta.foodtruck.domain.user.controller;

import com.sparta.foodtruck.domain.user.dto.*;
import com.sparta.foodtruck.domain.user.exception.CustomSameUsernameException;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.domain.user.service.UserService;
import com.sparta.foodtruck.global.dto.CustomStatusAndMessageResponseDto;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CustomStatusResponseDto> createAccount(@RequestBody @Valid SignupRequestDto requestDto){
        return userService.createAccount(requestDto);
    }

    @PostMapping("/signup/password")
    public ResponseEntity<CustomStatusResponseDto> passwordCheck(@RequestBody @Valid PasswordRequestDto requestDto){
        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }

    @GetMapping("/passwordcheck")
    public ResponseEntity<UsernameResponseDto> passwordCheck(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(new UsernameResponseDto(userDetails.getUsername()));
    }

    @PostMapping("/signup/username")
    public ResponseEntity<CustomStatusResponseDto> usernameCheck(@RequestBody UsernameVaildRequestDto requestDto){
        return userService.usernameCheck(requestDto);
    }

    @PostMapping("/signup/address")
    public ResponseEntity<CustomStatusResponseDto> addressCheck(@RequestBody @Valid AddressSameRequestDto requestDto){
        return userService.addressCheck(requestDto);
    }

    @GetMapping("/test")
    public Boolean addressCheck(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails == null;
    }

    @GetMapping("/test2")
    public Boolean addressCheck(){
        return true;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomStatusAndMessageResponseDto> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        CustomStatusAndMessageResponseDto responseDto = new CustomStatusAndMessageResponseDto(false);
        exception.getBindingResult().getFieldErrors().forEach(e -> responseDto.addMessage(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(responseDto);
    }

    @ExceptionHandler(CustomSameUsernameException.class)
    public ResponseEntity<CustomStatusResponseDto> CustomSameUsernameExceptionHandler(CustomSameUsernameException exception){
        CustomStatusResponseDto responseDto = new CustomStatusResponseDto(false);
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(responseDto);
    }
}
