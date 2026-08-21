package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.CoachTone;
import com.example.Healthcare_BE.user.entity.ExplanationLevel;
import com.example.Healthcare_BE.user.entity.RecommendationStyle;

/**
 * GET·PUT /api/users/me/ai-settings 요청/응답 공용 (signup_profile_api_spec.md 2-6).
 */
public record UserAiSettingsDto(
        RecommendationStyle recommendationStyle,
        ExplanationLevel explanationLevel,
        CoachTone coachTone,
        boolean autoDailyRoutine,
        boolean autoIntensityAdjust,
        boolean autoWeakpartAlert,
        boolean autoRestdaySuggest,
        boolean autoPostureTip
) {
}
