package com.example.Healthcare_BE.mealplan.service;

import com.example.Healthcare_BE.chat.entity.ChatMessage;
import com.example.Healthcare_BE.mealplan.dto.MealPlanDayDto;
import com.example.Healthcare_BE.mealplan.dto.MealPlanDto;
import com.example.Healthcare_BE.mealplan.dto.MealPlanMealDto;
import com.example.Healthcare_BE.mealplan.entity.MealPlan;
import com.example.Healthcare_BE.mealplan.entity.MealPlanDay;
import com.example.Healthcare_BE.mealplan.entity.MealPlanMeal;
import com.example.Healthcare_BE.mealplan.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    /** AI 응답의 result.mealPlan을 정규화된 테이블에 분해해 저장한다 (database.md 5장). */
    public void saveMealPlan(ChatMessage chatMessage, MealPlanDto dto) {
        MealPlan mealPlan = new MealPlan(chatMessage, dto.title());
        for (MealPlanDayDto dayDto : dto.days()) {
            MealPlanDay day = new MealPlanDay(dayDto.dayOfWeek());
            for (MealPlanMealDto mealDto : dayDto.meals()) {
                day.addMeal(new MealPlanMeal(
                        mealDto.slot(),
                        mealDto.menu(),
                        mealDto.calories(),
                        mealDto.carbsG(),
                        mealDto.proteinG(),
                        mealDto.fatG()));
            }
            mealPlan.addDay(day);
        }
        mealPlanRepository.save(mealPlan);
    }

    /** 세션 상세 조회 시 저장된 테이블을 다시 JSON 모양으로 조립한다 (api.md 3.4). */
    public Optional<MealPlanDto> findByChatMessage(UUID chatMessageId) {
        return mealPlanRepository.findByChatMessageId(chatMessageId).map(this::toDto);
    }

    private MealPlanDto toDto(MealPlan mealPlan) {
        List<MealPlanDayDto> days = mealPlan.getDays().stream()
                .map(day -> new MealPlanDayDto(
                        day.getDayOfWeek(),
                        day.getMeals().stream()
                                .map(meal -> new MealPlanMealDto(
                                        meal.getSlot(),
                                        meal.getMenu(),
                                        meal.getCalories(),
                                        meal.getCarbsG(),
                                        meal.getProteinG(),
                                        meal.getFatG()))
                                .toList()))
                .toList();
        return new MealPlanDto(mealPlan.getTitle(), days);
    }
}
