package com.example.Healthcare_BE.auth.controller;

import com.example.Healthcare_BE.auth.service.CustomOAuth2User;
import com.example.Healthcare_BE.auth.service.JwtProvider;
import com.example.Healthcare_BE.auth.service.OAuth2ExchangeCodeStore;
import com.example.Healthcare_BE.auth.service.RefreshTokenService;
import com.example.Healthcare_BE.auth.dto.AccessTokenResponse;
import com.example.Healthcare_BE.auth.dto.ExchangeRequest;
import com.example.Healthcare_BE.auth.dto.RefreshRequest;
import com.example.Healthcare_BE.auth.dto.TokenResponse;
import com.example.Healthcare_BE.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * /me는 1~2단계 검증용 임시 엔드포인트 — 구글 로그인 후 세션과 우리 DB 연동이 잘 됐는지 확인한다.
 * 4단계에서 세션 기반 인증이 JWT 검증 필터로 바뀌면 이 컨트롤러도 그 방식에 맞게 정리한다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final OAuth2ExchangeCodeStore exchangeCodeStore;

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal CustomOAuth2User principal) {
        if (principal == null) {
            return Map.of("loggedIn", false);
        }
        return Map.of(
                "loggedIn", true,
                "userId", principal.getUserId(),
                "attributes", principal.getAttributes());
    }

    /**
     * 로그인 성공 리다이렉트로 받은 일회용 code를 진짜 Access/Refresh Token으로 교환한다.
     */
    @PostMapping("/exchange")
    public TokenResponse exchange(@Valid @RequestBody ExchangeRequest request) {
        UUID userId = exchangeCodeStore.consume(request.code());
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = refreshTokenService.issue(userId);
        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public AccessTokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        User user = refreshTokenService.validate(request.refreshToken());
        String accessToken = jwtProvider.createAccessToken(user.getId());
        return new AccessTokenResponse(accessToken);
    }
}
