package com.example.Healthcare_BE.auth.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Authorization: Bearer <accessToken> 헤더를 검증해 SecurityContext에 인증 정보(principal = 유저 UUID)를 세팅한다.
 * 토큰이 없거나 유효하지 않아도 그냥 통과시킨다 — 실제 인증 요구는 SecurityConfig의 authorizeHttpRequests가 결정한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String accessToken = header.substring(BEARER_PREFIX.length());
            try {
                UUID userId = jwtProvider.validateAndGetUserId(accessToken);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userId, null, List.of()));
            } catch (JwtException e) {
                // 유효하지 않은/만료된 토큰은 무시하고 익명으로 진행한다.
            }
        }
        filterChain.doFilter(request, response);
    }
}
