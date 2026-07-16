package com.example.Healthcare_BE.mealplan.dto;

import java.util.List;

/**
 * api.md 4.3 result.mealPlan. ChatResultDto.mealPlan으로 그대로 실린다.
 */
public record MealPlanDto(
        String title,
        List<MealPlanDayDto> days
) {
}
