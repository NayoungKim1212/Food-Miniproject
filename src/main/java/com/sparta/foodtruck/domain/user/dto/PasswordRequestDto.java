package com.sparta.foodtruck.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequestDto {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z\\d]{8,25}$", message = "fail")
    private String password;
}
