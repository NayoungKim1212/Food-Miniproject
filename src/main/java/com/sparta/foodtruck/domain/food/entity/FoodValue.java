package com.sparta.foodtruck.domain.food.entity;

import com.sparta.foodtruck.domain.food.dto.FoodRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class FoodValue {

    @Id
    @Column(name = "food_id", nullable = false)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "food_id")
    Food food;

    Boolean salty;

    Boolean hot;

    Boolean night;

    Integer minSpicy;

    Integer maxSpicy;

    FoodWorldValue world;

    public FoodValue(Food food, Boolean salty, Boolean hot, Integer spicy, FoodWorldValue world) {
        this.food = food;
        this.salty = salty;
        this.hot = hot;
        this.minSpicy = spicy;
        this.maxSpicy = spicy;
        this.world = world;
    }


    public enum FoodWorldValue {
        KOREA(1),
        CHINA(2),
        JAPAN(3),
        AMERICA(4);

        private int value;

        FoodWorldValue(int value) {
            this.value = value;
        }

        public static FoodWorldValue findByNumber(int num) {
            for (FoodWorldValue foodWorldValue : FoodWorldValue.values()) {
                if (num == foodWorldValue.value) {
                    return foodWorldValue;
                }
            }
            throw new IllegalArgumentException("올바른 숫자가 없습니다.");
        }
        public static FoodWorldValue findByNumber(Integer num) {
            for (FoodWorldValue foodWorldValue : FoodWorldValue.values()) {
                if (num == foodWorldValue.value) {
                    return foodWorldValue;
                }
            }
            throw new IllegalArgumentException("올바른 숫자가 없습니다.");
        }
    }

    public FoodValue(FoodRequestDto requestDto) {
//        this.gender = requestDto.isGender();
        this.salty = requestDto.isSalty();
        this.minSpicy = requestDto.getSpicy();
        this.maxSpicy = requestDto.getSpicy();
        this.world = FoodWorldValue.findByNumber(requestDto.getWorld());
        this.hot = requestDto.isHot();

    }

}

