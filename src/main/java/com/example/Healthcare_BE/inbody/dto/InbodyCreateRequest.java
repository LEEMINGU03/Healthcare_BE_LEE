package com.example.Healthcare_BE.inbody.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * POST /api/inbody 요청 (signup_profile_api_spec.md 2-4).
 */
public record InbodyCreateRequest(
        @NotNull LocalDate measuredAt,
        @NotNull @Positive BigDecimal weightKg,
        @NotNull @Positive Integer bmrKcal,
        @NotNull @Positive BigDecimal skeletalMuscleMassKg,
        @NotNull @PositiveOrZero BigDecimal bodyFatMassKg,
        @PositiveOrZero BigDecimal bodyFatPct
) {
}
