package com.sparta.foodtruck.domain.food.dto;

import com.sparta.foodtruck.domain.food.entity.FoodComment;
import com.sparta.foodtruck.domain.user.dto.LoginRequestDto;
import com.sparta.foodtruck.domain.user.dto.PasswordRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private Long commentId;
    private String username;
    private String content;

    public CommentResponseDto(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public CommentResponseDto(FoodComment foodComment){
        this.commentId = foodComment.getId();
        this.username = foodComment.getUsername();
        this.content = foodComment.getContent();
    }

}
