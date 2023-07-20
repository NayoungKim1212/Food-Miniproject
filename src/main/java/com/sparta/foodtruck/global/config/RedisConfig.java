package com.sparta.foodtruck.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig { // Redis 데이터 베이스에 연결하고 데이터를 처리하기 위한 환경 구성

    // Spring 외부 설정 파일에서 Redis 호스트 및 포트 정보 읽어오기
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

//    @Value("${spring.data.redis.password}")
//    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
//        redisConfiguration.setPassword(password);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        // LettuceConnectionFactory 객체를 생성하고 RedisStandaloneConfiguration을 인자로 전달하여 연결 설정
        return lettuceConnectionFactory;
    }

    @Primary
    @Bean
    public RedisTemplate<String, String> redisTemplate() { // Redis 데이터를 처리하기 위한 핵심 클래스
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); // redisConnectionFactory()를 사용하여 Redis 연결 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // StringRedisSerializer를 키 및 값의 직렬화에 사용하도록 설정 --> Redis에 저장된 키도 문자열로 저장되고 검색됨
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}