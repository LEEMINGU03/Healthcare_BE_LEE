package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * POST /api/chat 요청(api.md 3.2). message는 sessionId가 null일 때만 비워둘 수 있다
 * (인사말 요청) — 이 교차 필드 규칙은 bean validation으로 표현할 수 없어 서비스 계층에서 검증한다.
 */
public record ChatRequest(
        @NotNull ChatType type,
        String message,
        UUID sessionId,
        @Valid ChatSettingsRequest settings
) {
}
