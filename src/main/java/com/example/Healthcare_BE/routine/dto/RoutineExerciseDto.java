package com.example.Healthcare_BE.routine.dto;

/**
 * api.md 4.2 result.routine.exercises[] 원소. JSON 필드명은 order — 엔티티의 orderNo와
 * 이름이 다르니 매핑 시 주의.
 */
public record RoutineExerciseDto(
        Integer order,
        String name,
        String sets,
        String reps,
        String description,
        String imageUrl
) {
}
