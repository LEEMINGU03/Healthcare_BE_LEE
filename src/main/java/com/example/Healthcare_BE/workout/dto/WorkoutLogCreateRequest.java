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
 * POST /api/workout-logs 요청. routineExerciseId가 있으면 AI 루틴의 특정 운동을 수행한
 * 기록 — 이때는 그 운동의 body_part로부터 muscle_group을 백엔드가 계산해 채우고,
 * muscleGroup 필드는 무시한다. routineExerciseId가 없으면 자유 입력이며 muscleGroup을
 * 그대로 쓴다. routineId는 이전부터 있던 필드로, routineExerciseId 없이도 루틴 자체와만
 * 연결하고 싶을 때 계속 쓸 수 있다(하위 호환) — 이 경우 muscle_group 자동 계산은 없다.
 */
public record WorkoutLogCreateRequest(
        @NotNull LocalDate performedAt,
        @NotBlank String exerciseName,
        MuscleGroup muscleGroup,
        @Positive Integer plannedSets,
        @PositiveOrZero Integer completedSets,
        @Positive Integer reps,
        @PositiveOrZero BigDecimal weightKg,
        UUID routineId,
        UUID routineExerciseId
) {
}
