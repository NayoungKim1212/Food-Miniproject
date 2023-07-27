package com.sparta.foodtruck.global.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsServiceImpl;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sparta.foodtruck.global.custom.CustomStaticMethodClass.setFailResponse;
import static com.sparta.foodtruck.global.jwt.JwtUtil.ACCESS_HEADER;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        //String tokenValue = jwtUtil.getTokenFromRequest(req);
        log.info("Token Filter");
        String tokenValue = jwtUtil.getTokenFromRequestHeader(JwtUtil.ACCESS_HEADER, req);
        String tokenValueRefresh = jwtUtil.getTokenFromRequestHeader(JwtUtil.AUTHORIZATION_HEADER, req);

        if (StringUtils.hasText(tokenValue)) {
            log.info("AccessToken");
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            try {
                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token Error");
                    return;
                }
            } catch (Exception e) {
                exceptionHandlerAccess(res, e);
                return;
            }

            log.info("Vaildate Correct");
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                exceptionHandler(res, e);
                return;
            }
        }
        else if (StringUtils.hasText(tokenValueRefresh)) {
            GetRefreshToken(req, res, tokenValueRefresh);
        } else{
            res.addHeader("AccessTokenDenide","true");
        }

        filterChain.doFilter(req, res);
    }

    private void GetRefreshToken(HttpServletRequest req, HttpServletResponse res, String tokenValue) throws IOException{
        if (StringUtils.hasText(tokenValue)) {
            log.info("RefreshToken");
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);
            try {
                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token Error");
                    return;
                }
            } catch (Exception e) {
                exceptionHandlerRefresh(res, e);
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            String subject = info.getSubject();
            String uuid = userDetailsService.decryptAES(subject);
            String username = userDetailsService.loadUsernameByRedis(uuid);
            try {
                setAuthenticationRefresh(res, username);
            } catch (Exception e) {
                exceptionHandler(res, e);
                return;
            }
        }
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void setAuthenticationRefresh(HttpServletResponse response, String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthenticationRefresh(response, username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByAccountInfo(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Authentication createAuthenticationRefresh(HttpServletResponse response, String username) {
        UserDetails userDetails = userDetailsService.loadUserByAccountInfo(username);
        String accessToken = jwtUtil.createToken(userDetails.getUsername(),((UserDetailsImpl) userDetails).getAccountInfo().getRole());
        jwtUtil.addJwtToHeader(accessToken, ACCESS_HEADER, response);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void exceptionHandler(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        setFailResponse(response, messageDto);
    }

    private void exceptionHandlerRefresh(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        messageDto.setRefreshValidationError(true);
        setFailResponse(response, messageDto);
    }

    private void exceptionHandlerAccess(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler Access");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        messageDto.setAccessValidationError(true);
        setFailResponse(response, messageDto);
    }


}