package com.sparta.foodtruck.domain.food.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateFoodRequestDto {

    String foodName;

    String imageUrl;

    Boolean salty;

    Boolean hot;

    Integer spicy;
    // 한식 1 / 중식 2 / 일식 3 / 양식 4
    Integer world;
}
