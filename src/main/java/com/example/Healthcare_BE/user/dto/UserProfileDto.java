package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.WorkoutType;

import java.math.BigDecimal;

/**
 * AI 서버 요청의 profile 필드(api.md 4.1)로 그대로 직렬화된다.
 * 프론트에 노출하는 전용 API는 없다 — 인사말도 채팅의 일부로 AI가 생성한다(api.md 3.2).
 */
public record UserProfileDto(
        String name,
        Gender gender,
        BigDecimal heightCm,
        WorkoutType previousWorkout
) {
}
