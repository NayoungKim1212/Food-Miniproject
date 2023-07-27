package com.sparta.foodtruck.domain.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoodRequestDto {

    private boolean gender;
    private boolean salty;
    private int spicy;
    private int world;
    private boolean hot;
}
