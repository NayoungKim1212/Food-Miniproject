package com.sparta.foodtruck.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE) : 필터의 우선 순위 설정
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    // 필터 초기화
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 모든 요청이나 응답이 발생할 때마다 호출, CORS 관련 헤더 설정 및 OPTIONS 요청에 대한 처리 수행
        HttpServletRequest request = (HttpServletRequest) req; // 요청 객체를 HttpServletRequest로 형변환
        HttpServletResponse response = (HttpServletResponse) res; // 응답 객체를 HttpServletResponse로 형변환

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // http://localhost:3000 도메인에서 오는 요청 허용
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 응답 헤더에 자격증명 허용 여부 설정
        response.setHeader("Access-Control-Allow-Methods","*"); // 응답 헤더에 허용된 HTTP 메서드 설정, 모든 메서드를 허용하기 위해 "*" 사용
        response.setHeader("Access-Control-Max-Age", "3600"); // 응답 헤더에 사전 요청의 유효 시간 설정(1시간으로 설정)
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization"); // 응답 헤더에 허용된 요청 헤더 설정

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) { // 요청이 OPTIONS 메서드인 경우
            response.setStatus(HttpServletResponse.SC_OK); // 응답 상태 SC_OK(200)로 설정하여 사전 요청의 응답 처리
        }else {
            chain.doFilter(req, res); // 필터 체인의 다음 필터로 요청과 응답 전달
        }
    }

    @Override
    public void destroy() {
    // 필터가 제거될 때 호출되는 메서드
    // 필터가 사용한 리소스를 정리하거나 후처리 작업을 수행할 수 있음
    }
}