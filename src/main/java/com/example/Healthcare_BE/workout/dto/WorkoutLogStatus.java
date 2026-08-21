package com.example.Healthcare_BE.workout.dto;

/**
 * GET /api/workout-logs 응답의 계산 필드. DB에 저장하지 않고 조회 시점에
 * completionRate로부터 계산한다 — 임계값이 바뀌어도 과거 기록을 재계산할 필요가 없다.
 */
public enum WorkoutLogStatus {
    COMPLETED, INCOMPLETE
}
