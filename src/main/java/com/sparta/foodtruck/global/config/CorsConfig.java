package com.sparta.foodtruck.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig { // CORS 필터 구성
    @Bean
    public CorsFilter corsFilter() { // CorsFilter 객체를 생성하고 반환하는 메서드
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 패턴에 기반한 CORS 구성 제공
        CorsConfiguration config = new CorsConfiguration(); // CORS 구성을 설정하는데 사용되는 객체 생성
        config.setAllowCredentials(true); // 자격 증명 허용 여부 설정
        config.addAllowedOriginPattern("*"); // 허용된 도메인 패턴 설정(모든 도메인을 허용하기 위해 "*" 사용)
        config.addAllowedHeader("*"); // 허용된 요청 헤더 설정(모든 헤더를 허용하기 위해 "*" 설정)
        config.addAllowedMethod("*"); // 허용된 HTTP 메서드 설정(모든 메서드를 허용하기 위해 "*" 설정)
        config.addExposedHeader("*"); // https://iyk2h.tistory.com/184?category=875351 // 헤더값 보내줄 거 설정.
        // 노출할 응답 헤더 설정(모든 헤더를 노출하기 위해 "*" 설정)
        source.registerCorsConfiguration("/**",config); // UrlBasedCorsConfigurationSource에 CORS 구성 등록
        // 모든 URL에 대해 CORS 구성을 적용하도록 설정하기 위해 "/**" 사용
        return new CorsFilter(source); // 생성한 UrlBasedCorsConfigurationSource를 사용하여 CorsFilter 객체 생성
    }
}