package com.example.Healthcare_BE.mealplan.dto;

import com.example.Healthcare_BE.mealplan.entity.MealPlanDayOfWeek;

import java.util.List;

/**
 * api.md 4.3 result.mealPlan.days[] 원소.
 */
public record MealPlanDayDto(
        MealPlanDayOfWeek dayOfWeek,
        List<MealPlanMealDto> meals
) {
}
