package com.example.Healthcare_BE.workout.dto;

import com.example.Healthcare_BE.workout.entity.MuscleGroup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * POST/GET /api/workout-logs 응답. status/completionRate는 저장된 값이 아니라
 * plannedSets/completedSets로부터 조회 시점에 계산된 값이다.
 */
public record WorkoutLogResponse(
        UUID id,
        UUID routineId,
        UUID routineExerciseId,
        LocalDate performedAt,
        String exerciseName,
        MuscleGroup muscleGroup,
        Integer plannedSets,
        Integer completedSets,
        Integer reps,
        BigDecimal weightKg,
        BigDecimal completionRate,
        WorkoutLogStatus status
) {
}
