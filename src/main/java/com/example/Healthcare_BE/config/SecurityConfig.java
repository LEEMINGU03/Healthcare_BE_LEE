package com.example.Healthcare_BE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * MVP는 로그인이 없다 — 전 요청 permitAll (architecture.md 6장).
 * 세션/쿠키 기반 인증이 없는 stateless JSON API라 CSRF 보호 대상이 아니어서 비활성화한다.
 * 인증 도입 시 이 지점만 JWT 검증으로 교체한다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
