package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.InjuryPart;
import com.example.Healthcare_BE.user.entity.PreferredWorkoutType;

import java.util.List;

/**
 * GET·PUT /api/users/me/preferences 요청/응답 공용 (signup_profile_api_spec.md 2-5).
 * InbodyRecentResponse처럼 요청·응답 모양이 동일해 하나로 재사용한다.
 */
public record UserPreferencesDto(
        List<PreferredWorkoutType> preferredWorkoutTypes,
        List<InjuryPart> injuryParts
) {
}
