package com.sparta.foodtruck.domain.food.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Long contentid;
    private String username;
    private String content;
}
