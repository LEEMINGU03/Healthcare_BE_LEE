package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.ExperienceLevel;
import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.Goal;
import com.example.Healthcare_BE.user.entity.WorkoutDuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * GET/PUT/PATCH /api/users/me 공통 응답 (signup_profile_api_spec.md 2-1~2-3).
 */
public record UserMeResponse(
        boolean profileCompleted,
        String name,
        String nickname,
        Gender gender,
        LocalDate birthDate,
        String email,
        String phone,
        String bio,
        String profileImageUrl,
        BigDecimal heightCm,
        List<Goal> goals,
        ExperienceLevel experienceLevel,
        Integer workoutFrequencyPerWeek,
        WorkoutDuration workoutDuration,
        BigDecimal targetWeightKg,
        BigDecimal targetMuscleKg,
        LocalDate goalTargetDate
) {
}
