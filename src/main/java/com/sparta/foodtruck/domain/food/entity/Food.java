package com.sparta.foodtruck.domain.food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String foodName;

    String imageUrl;

    Long selectBy;


    public Food(String foodName, String imageUrl) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.selectBy = 0L;
    }

}
