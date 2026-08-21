package com.example.Healthcare_BE.user.dto;

import com.example.Healthcare_BE.user.entity.ReceiveChannel;

/**
 * GET·PUT /api/users/me/notification-settings 요청/응답 공용 (signup_profile_api_spec.md 2-7).
 */
public record UserNotificationSettingsDto(
        boolean notifyWorkoutStart,
        boolean notifyWeeklyGoal,
        boolean notifyLongAbsence,
        boolean notifyAiRecommend,
        boolean notifyBodyUpdate,
        boolean notifyWorkoutSummary,
        boolean notifyServiceEvent,
        ReceiveChannel receiveChannel
) {
}
