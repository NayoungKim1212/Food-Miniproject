package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.entity.FoodComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodCommentRepository extends JpaRepository<FoodComment, Long> {
    List<FoodComment> findByFoodId(Long foodId);
}
