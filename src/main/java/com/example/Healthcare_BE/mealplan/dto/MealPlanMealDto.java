package com.example.Healthcare_BE.mealplan.dto;

import com.example.Healthcare_BE.mealplan.entity.MealSlot;

import java.math.BigDecimal;

/**
 * api.md 4.3 result.mealPlan.days[].meals[] 원소.
 */
public record MealPlanMealDto(
        MealSlot slot,
        String menu,
        Integer calories,
        BigDecimal carbsG,
        BigDecimal proteinG,
        BigDecimal fatG
) {
}
