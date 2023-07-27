package com.sparta.foodtruck.domain.food.entity;

import lombok.Getter;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter
public class TempResult {
    UUID uuid;
    Food selectFood;
    private LocalDateTime localDateTime;

    public UUID generateUUID() {
        return UUID.randomUUID();
    }
    public TempResult() {
        this.uuid = generateUUID();
        this.localDateTime = LocalDateTime.now().plusMinutes(10);
    }
}
