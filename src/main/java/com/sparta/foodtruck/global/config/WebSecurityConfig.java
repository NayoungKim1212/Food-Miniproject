package com.sparta.foodtruck.global.config;


import com.sparta.foodtruck.domain.user.sercurity.UserDetailsServiceImpl;
import com.sparta.foodtruck.global.filter.CustomAuthenticationEntryPoint;
import com.sparta.foodtruck.global.jwt.JwtAuthenticationFilter;
import com.sparta.foodtruck.global.jwt.JwtAuthorizationFilter;
import com.sparta.foodtruck.global.jwt.JwtUtil;
import com.sparta.foodtruck.global.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig { // 이 개 같은거 설명좀 해주실 분 ?

    private final JwtUtil jwtUtil;
    private final CorsConfig corsConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Autowired
    public WebSecurityConfig(JwtUtil jwtUtil, CorsConfig corsConfig,
                             PasswordEncoder passwordEncoder,
                             UserDetailsServiceImpl userDetailsService,
                             AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.corsConfig = corsConfig;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(jwtUtil, userDetailsService);
        return filter;
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider bean = new DaoAuthenticationProvider();
        bean.setHideUserNotFoundExceptions(false);
        bean.setUserDetailsService(userDetailsService);
        bean.setPasswordEncoder(passwordEncoder);
        return bean;
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            web.ignoring()
//                    .requestMatchers(new AntPathRequestMatcher("/api/food/**"));
//        };
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable()); // CSRF 보안 기능 비활성화 => 토큰을 요구하지 않고 요청 처리 가능

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 => 서버의 상태를 유지하지 않고 인증 수행
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) -> // HTTP 요청에 대한 인가(Authorization) 설정 수행
                authorizeHttpRequests // 여러 경로 및 규칙 지정 가능
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정(정적 리소스에 대한 요청 모두 허용)
                        .requestMatchers("/api/user/**").permitAll() // '/api/users/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers(HttpMethod.GET, "/api/food/**").permitAll() // '/api/food/'로 시작하고 GET 메소드인 요청에 대해 모두 허용
                        .requestMatchers("/api/food/result/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/food/*/choice").permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.formLogin(Customizer.withDefaults()); // 기본적인 폼 로그인 기능을 사용, Customizer.withDefaults() : 기본 설정 사용

        // exceptionHandling 처리
        http.exceptionHandling((httpSecurityExceptionHandlingConfigurer) ->
                httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        //http.cors().configurationSource(corsConfigurationSource()) //---------- (1)
        http.addFilterBefore(corsConfig.corsFilter(), JwtAuthenticationFilter.class); // SPRING 3.0

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}