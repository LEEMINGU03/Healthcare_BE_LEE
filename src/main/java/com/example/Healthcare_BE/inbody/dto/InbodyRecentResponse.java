package com.example.Healthcare_BE.inbody.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * GET /api/inbody/recent 응답(api.md 3.1). AI 서버 요청의 inbody 필드(api.md 4.1)로도
 * 그대로 재사용된다 — 두 계약의 모양이 동일하다.
 */
public record InbodyRecentResponse(
        LocalDate measuredAt,
        BigDecimal weightKg,
        BigDecimal skeletalMuscleMassKg,
        BigDecimal bodyFatMassKg,
        Integer bmrKcal
) {
}
