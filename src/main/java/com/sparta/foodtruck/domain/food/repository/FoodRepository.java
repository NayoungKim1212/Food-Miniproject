package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findAllByOrderBySelectByDesc(Pageable pageable);
}
