package com.sparta.foodtruck.domain.user.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressSameRequestDto {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z\\d]{4,12}$", message = "id not allow")
    String address;
}
