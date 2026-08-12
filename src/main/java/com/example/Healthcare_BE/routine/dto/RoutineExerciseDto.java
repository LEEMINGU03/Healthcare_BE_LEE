package com.example.Healthcare_BE.routine.dto;

/**
 * api.md 4.2 result.routine.exercises[] 원소. JSON 필드명은 order — 엔티티의 orderNo와
 * 이름이 다르니 매핑 시 주의.
 *
 * bodyPart는 일부러 enum이 아닌 String이다 — AI가 분류를 바꾸거나 9개 밖의 값을 보내도
 * Jackson 역직렬화가 깨지지 않게 하기 위함. 값 검증은 DB CHECK(routine_exercises.body_part)에
 * 맡긴다.
 */
public record RoutineExerciseDto(
        Integer order,
        String name,
        String sets,
        String reps,
        String description,
        String imageUrl,
        String bodyPart
) {
}
