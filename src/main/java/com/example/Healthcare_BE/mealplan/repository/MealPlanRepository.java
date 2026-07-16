package com.example.Healthcare_BE.mealplan.repository;

import com.example.Healthcare_BE.mealplan.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MealPlanRepository extends JpaRepository<MealPlan, UUID> {

    Optional<MealPlan> findByChatMessageId(UUID chatMessageId);
}
