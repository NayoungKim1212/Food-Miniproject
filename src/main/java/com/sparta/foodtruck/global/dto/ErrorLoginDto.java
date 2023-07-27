package com.sparta.foodtruck.global.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorLoginDto {
    Boolean idCheck;
    Boolean pwCheck;

    public ErrorLoginDto() {
        this.idCheck = false;
        this.pwCheck = false;
    }
}
