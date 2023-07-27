package com.sparta.foodtruck.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z\\d]{4,12}$", message = "id not allow")
    private String address;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z\\d]{8,25}$", message = "password not allow")
    private String password;
    @NotNull
    private String username;
}
