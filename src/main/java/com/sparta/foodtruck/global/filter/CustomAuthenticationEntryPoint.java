package com.sparta.foodtruck.global.filter;

import com.sparta.foodtruck.global.custom.CustomStaticMethodClass;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("가입되지 않은 사용자 접근");
//        String temp = response.getHeader("AccessTokenDenide");
//        if(temp != null)
//            CustomStaticMethodClass.setFailResponse(response, new ErrorLoginMessageDto("RefreshToken Redirect", true));
//        else
        response.setStatus(401);
//      response.sendRedirect("/login");
    }
}