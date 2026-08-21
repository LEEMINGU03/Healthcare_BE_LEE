package com.example.Healthcare_BE.routine.dto;

import java.util.List;

/**
 * api.md 4.2 result.routine. ChatResultDto.routine으로 그대로 실린다.
 */
public record RoutineDto(
        String title,
        List<RoutineExerciseDto> exercises
) {
}
