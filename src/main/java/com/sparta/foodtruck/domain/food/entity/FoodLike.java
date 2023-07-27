package com.sparta.foodtruck.domain.food.entity;

import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "username")
    AccountInfo accountInfo;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    public FoodLike(AccountInfo accountInfo, Food food) {
        this.accountInfo = accountInfo;
        this.food = food;
    }

}
