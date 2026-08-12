package com.example.Healthcare_BE.workout.dto;

import com.example.Healthcare_BE.workout.entity.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * POST /api/workout-logs 요청. routineId가 있으면 AI 루틴 수행 기록, 없으면 자유 입력이다.
 */
public record WorkoutLogCreateRequest(
        @NotNull LocalDate performedAt,
        @NotBlank String exerciseName,
        MuscleGroup muscleGroup,
        @Positive Integer plannedSets,
        @PositiveOrZero Integer completedSets,
        @Positive Integer reps,
        @PositiveOrZero BigDecimal weightKg,
        UUID routineId
) {
}
