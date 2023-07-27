package com.sparta.foodtruck.domain.food.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentListResponseDto {
    Boolean userLike;
    List<CommentResponseDto> data;
}
