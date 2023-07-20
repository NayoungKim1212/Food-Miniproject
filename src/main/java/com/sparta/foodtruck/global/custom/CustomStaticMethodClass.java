package com.sparta.foodtruck.global.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j // 로깅 기능 추가
public class CustomStaticMethodClass { // HTTP 응답을 설정하여 인증 실패 시 클라이언트에게 실패 응답을 보내는 역할

    public static void setFailResponse(HttpServletResponse response, ErrorLoginMessageDto errorLoginDto) throws IOException {
        // 실패 응답을 설정하는 정적 메서드(응답 설정, 실패 응답에 대한 정보 포함)
        log.info("set Fail Response");
        response.setStatus(401); // 인증 실패 시 응답의 상태 코드 401
        response.setContentType("application/json"); // 응답의 콘텐츠 유형을 JSON으로 설정, 클라이언트가 JSON 형식의 응답을 받을 수 있도록 함
        response.setCharacterEncoding("UTF-8"); // 응답의 문자 인코딩을 UTF-8로 설정 --> 한국어와 같은 다국어 문자를 올바르게 처리 가능
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson 라이브러리의 ObjectMapper 객체 생성 => JSON 직렬화 및 역질렬화 담당
        String str = objectMapper.writeValueAsString(errorLoginDto); // eroorLoginDto 객체를 JSON 문자열로 직렬화
        response.getWriter().write(str); // 직렬화 된 JSON 문자열을 응답의 출력 스트림을 통해 클라이언트에게 전송
    }
}
