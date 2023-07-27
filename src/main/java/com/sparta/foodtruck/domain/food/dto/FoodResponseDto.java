package com.sparta.foodtruck.domain.food.dto;

import com.sparta.foodtruck.domain.food.entity.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodResponseDto {

    private Long id;
    private String name;
    private String imageUrl;


    public FoodResponseDto(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public FoodResponseDto(Food food) {
        this.id = food.getId();
        this.name = food.getFoodName();
        this.imageUrl = food.getImageUrl();
    }

}
