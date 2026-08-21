package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.ExperienceLevel;
import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.Goal;
import com.example.Healthcare_BE.user.entity.WorkoutDuration;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * PUT /api/users/me — 회원가입1 통째 저장 (signup_profile_api_spec.md 2-2).
 * enum 필드(goals/experienceLevel/workoutDuration)의 허용값 검증은 Jackson 역직렬화가
 * 실제 enum 타입으로 하며, 실패 시 problemdetails.enabled=true 설정으로 자동 400 처리된다.
 */
public record UserProfileRequest(
        String nickname,
        @NotNull Gender gender,
        LocalDate birthDate,
        String email,
        String phone,
        String bio,
        String profileImageUrl,
        @NotNull @Positive BigDecimal heightCm,
        List<Goal> goals,
        ExperienceLevel experienceLevel,
        @Min(1) @Max(7) Integer workoutFrequencyPerWeek,
        WorkoutDuration workoutDuration,
        BigDecimal targetWeightKg,
        BigDecimal targetMuscleKg,
        LocalDate goalTargetDate
) {
}
