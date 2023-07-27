package com.sparta.foodtruck.domain.food.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private String username;
    private String content;

    public CommentDto(String username, String content) {
        this.username = username;
        this.content = content;
    }
}
