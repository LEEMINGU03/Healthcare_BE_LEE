package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.mealplan.dto.MealPlanDto;
import com.example.Healthcare_BE.routine.dto.RoutineDto;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * api.md 3.2/4.1의 result. type이 COACHING이면 routine만, NUTRITION이면 mealPlan만
 * 채워진다 — null 필드는 직렬화에서 제외해 문서의 JSON 예시와 모양을 맞춘다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatResultDto(
        RoutineDto routine,
        MealPlanDto mealPlan
) {
}
