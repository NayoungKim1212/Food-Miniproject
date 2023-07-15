package com.sparta.foodtruck.domain.food.entity;


import com.sparta.foodtruck.domain.food.dto.CommentRequestDto;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class FoodComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    @ManyToOne
    @JoinColumn(name = "account_id")
    AccountInfo accountInfo;

    String content;
    String username;

    public FoodComment(Food food, String username, String content) {
        this.food = food;
        this.username = username;
        this.content = content;
    }


    public void add(CommentRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.content = requestDto.getContent();
    }
}
