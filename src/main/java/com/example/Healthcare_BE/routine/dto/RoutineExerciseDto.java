package com.example.Healthcare_BE.routine.dto;

import java.util.UUID;

/**
 * api.md 4.2 result.routine.exercises[] 원소. JSON 필드명은 order — 엔티티의 orderNo와
 * 이름이 다르니 매핑 시 주의.
 *
 * bodyPart는 일부러 enum이 아닌 String이다 — AI가 분류를 바꾸거나 9개 밖의 값을 보내도
 * Jackson 역직렬화가 깨지지 않게 하기 위함. 값 검증은 DB CHECK(routine_exercises.body_part)에
 * 맡긴다.
 *
 * id는 AI 응답 파싱 시에는 항상 null이다(AI는 DB id를 모른다) — 저장 후 세션 상세 조회로
 * 재조립할 때만 채워진다. 프론트는 이 id를 POST /api/workout-logs의 routineExerciseId로
 * 그대로 돌려보낸다 (api.md 3.5 참고).
 */
public record RoutineExerciseDto(
        UUID id,
        Integer order,
        String name,
        String sets,
        String reps,
        String description,
        String imageUrl,
        String bodyPart
) {
}
