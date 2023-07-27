package com.sparta.foodtruck.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    public String getValues(String key) {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

//    public String getKey(String value) {
//        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        return values.get(key);
//    }

    public void setValues(String key, String value) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    public void setValuesByExpireSecond(String key, String value, Long duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        values.set(key, value, expireDuration);
    }

    public void setValuesByExpireDay(String key, String value, Long duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofDays(duration);
        values.set(key, value, expireDuration);
    }

    public boolean existKey(String key) {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key) == null;
    }

//    public void setSets(String key, String... values) {
//        redisTemplate.opsForSet().add(key, values);
//    }
//
//    public Set getSets(String key) {
//        return redisTemplate.opsForSet().members(key);
//    }


}