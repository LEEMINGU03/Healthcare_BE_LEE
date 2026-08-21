package com.example.Healthcare_BE.chat.dto;

import jakarta.validation.constraints.Positive;

/**
 * api.md 3.2 요청의 settings, 4.1 AI 요청의 settings 양쪽에서 공유된다 — 모양이 동일하다.
 */
public record ChatSettingsRequest(
        String upperBody,
        String lowerBody,
        @Positive Integer durationMinutes
) {
}
