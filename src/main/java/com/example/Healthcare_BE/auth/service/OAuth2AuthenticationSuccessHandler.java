package com.example.Healthcare_BE.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

/**
 * 구글 로그인 성공 시, 실제 토큰이 URL에 노출되지 않도록 60초짜리 일회용 교환 code만 발급해서
 * 프론트 콜백 페이지로 리다이렉트한다. 프론트는 이 code를 POST /api/auth/exchange로 바꿔서
 * 진짜 Access/Refresh Token을 응답 본문으로 받는다.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2ExchangeCodeStore exchangeCodeStore;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
        UUID userId = principal.getUserId();

        String code = exchangeCodeStore.issue(userId);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("code", code)
                .build()
                .toUriString();

        response.sendRedirect(targetUrl);
    }
}
