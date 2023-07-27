package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.entity.Food;
import com.sparta.foodtruck.domain.food.entity.FoodLike;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long> {
    FoodLike findByAccountInfoAndFood(AccountInfo accountInfo, Food food);
}
