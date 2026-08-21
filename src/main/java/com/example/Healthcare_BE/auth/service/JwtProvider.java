package com.example.Healthcare_BE.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Access Token과 Refresh Token 둘 다 JWT다 — 서명/만료 검증 방식은 같고 만료 기간만 다르다.
 * Refresh Token은 여기서 서명 검증만 하고, DB 저장 여부(로그아웃 시 무효화)는 RefreshTokenService가 담당한다.
 */
@Component  
public class JwtProvider {

    private final SecretKey key;
    private final Duration accessTokenExpiry;
    private final Duration refreshTokenExpiry;

    public JwtProvider(@Value("${app.jwt.secret}") String secret,
                        @Value("${app.jwt.access-token-expiry-minutes}") long accessTokenExpiryMinutes,
                        @Value("${app.jwt.refresh-token-expiry-days}") long refreshTokenExpiryDays) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpiry = Duration.ofMinutes(accessTokenExpiryMinutes);
        this.refreshTokenExpiry = Duration.ofDays(refreshTokenExpiryDays);
    }

    public String createAccessToken(UUID userId) {
        return createToken(userId, accessTokenExpiry);
    }

    public String createRefreshToken(UUID userId) {
        return createToken(userId, refreshTokenExpiry);
    }

    private String createToken(UUID userId, Duration expiry) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiry)))
                .signWith(key)
                .compact();
    }

    /**
     * 서명·만료를 검증하고 유저 id를 꺼낸다. 유효하지 않으면 io.jsonwebtoken.JwtException이 던져진다.
     */
    public UUID validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }
}
