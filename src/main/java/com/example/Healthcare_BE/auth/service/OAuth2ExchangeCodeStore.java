package com.example.Healthcare_BE.auth.service;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 로그인 성공 → 프론트 리다이렉트 사이에 실제 토큰이 URL에 노출되지 않도록,
 * 짧게만 유효한 일회용 교환 code를 발급/소비한다. 서버 재시작 시 사라져도 무방할 만큼
 * 수명이 짧아(초 단위) DB가 아니라 메모리에 둔다.
 */
@Component
public class OAuth2ExchangeCodeStore {

    private static final Duration CODE_TTL = Duration.ofSeconds(60);

    private final Map<String, CodeEntry> codes = new ConcurrentHashMap<>();

    public String issue(UUID userId) {
        String code = UUID.randomUUID().toString();
        codes.put(code, new CodeEntry(userId, Instant.now().plus(CODE_TTL)));
        return code;
    }

    /**
     * code를 한 번 소비하면 즉시 제거한다(재사용 불가). 없거나 만료됐으면 예외를 던진다.
     */
    public UUID consume(String code) {
        CodeEntry entry = codes.remove(code);
        if (entry == null || entry.expiresAt().isBefore(Instant.now())) {
            throw new InvalidExchangeCodeException("유효하지 않거나 만료된 code입니다.");
        }
        return entry.userId();
    }

    private record CodeEntry(UUID userId, Instant expiresAt) {
    }
}
