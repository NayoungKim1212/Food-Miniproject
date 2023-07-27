package com.sparta.foodtruck.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.foodtruck.domain.user.dto.LoginRequestDto;
import com.sparta.foodtruck.domain.user.dto.UsernameVaildRequestDto;
import com.sparta.foodtruck.domain.user.entity.UserRoleEnum;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsServiceImpl;
import com.sparta.foodtruck.global.dto.ErrorLoginDto;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.util.UUID;

import static com.sparta.foodtruck.global.custom.CustomStaticMethodClass.setFailResponse;
import static com.sparta.foodtruck.global.jwt.JwtUtil.*;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate( // authenticate method
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getAddress(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getAccountInfo().getRole();

        String token = jwtUtil.createToken(username, role);
        //jwtUtil.addJwtToCookie(token, response);
        jwtUtil.addJwtToHeader(token, ACCESS_HEADER, response);

        MakeRefreshToken(response, username);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(new UsernameVaildRequestDto(username));
        response.getWriter().write(str);
    }

    private void MakeRefreshToken(HttpServletResponse response, String username){
        String redisKey = UUID.randomUUID().toString();
        //userDetailsService.saveUsernameByRedis(redisKey, username);
        userDetailsService.saveUsernameByRedisExpireDay(redisKey, username, 1L);
        redisKey = userDetailsService.encryptAES(redisKey);
        String token = jwtUtil.createTokenRefresh(redisKey);
        jwtUtil.addJwtToHeader(token, AUTHORIZATION_HEADER, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        // fail error
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if(failed instanceof UsernameNotFoundException){
            messageDto.setMessage("아이디가 일치하지 않습니다.");
        } else if (failed instanceof BadCredentialsException) {
            messageDto.setMessage("패스워드가 일치하지 않습니다.");
        }
        setFailResponse(response, messageDto);
    }


}