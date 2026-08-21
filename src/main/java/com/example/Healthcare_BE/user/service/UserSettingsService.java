package com.example.Healthcare_BE.user.service;

import com.example.Healthcare_BE.user.dto.UserAiSettingsDto;
import com.example.Healthcare_BE.user.dto.UserNotificationSettingsDto;
import com.example.Healthcare_BE.user.dto.UserPreferencesDto;
import com.example.Healthcare_BE.user.entity.InjuryPart;
import com.example.Healthcare_BE.user.entity.PreferredWorkoutType;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.entity.UserAiSettings;
import com.example.Healthcare_BE.user.entity.UserNotificationSettings;
import com.example.Healthcare_BE.user.entity.UserPreferences;
import com.example.Healthcare_BE.user.repository.UserAiSettingsRepository;
import com.example.Healthcare_BE.user.repository.UserNotificationSettingsRepository;
import com.example.Healthcare_BE.user.repository.UserPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 설정 3종 GET·PUT (signup_profile_api_spec.md 2-5~2-7). 셋 다 유저당 1행(findByUserId)에 대한
 * upsert라 로직 모양이 같아 한 서비스에 묶는다. GET 시 레코드가 없으면 엔티티 기본값을 그대로 응답한다
 * (미결사항 확정: DB 생성은 PUT에서만 — 트랜지언트 엔티티는 저장하지 않고 매핑만 재사용).
 */
@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserService userService;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserAiSettingsRepository userAiSettingsRepository;
    private final UserNotificationSettingsRepository userNotificationSettingsRepository;

    public UserPreferencesDto getPreferences() {
        User user = userService.getCurrentUser();
        UserPreferences preferences = userPreferencesRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserPreferences(user));
        return toDto(preferences);
    }

    @Transactional
    public UserPreferencesDto putPreferences(UserPreferencesDto request) {
        User user = userService.getCurrentUser();
        UserPreferences preferences = userPreferencesRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserPreferences(user));
        preferences.updatePreferences(toStringList(request.preferredWorkoutTypes()), toStringList(request.injuryParts()));
        userPreferencesRepository.save(preferences);
        return toDto(preferences);
    }

    public UserAiSettingsDto getAiSettings() {
        User user = userService.getCurrentUser();
        UserAiSettings settings = userAiSettingsRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserAiSettings(user));
        return toDto(settings);
    }

    @Transactional
    public UserAiSettingsDto putAiSettings(UserAiSettingsDto request) {
        User user = userService.getCurrentUser();
        UserAiSettings settings = userAiSettingsRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserAiSettings(user));
        settings.updateSettings(request.recommendationStyle(), request.explanationLevel(), request.coachTone(),
                request.autoDailyRoutine(), request.autoIntensityAdjust(), request.autoWeakpartAlert(),
                request.autoRestdaySuggest(), request.autoPostureTip());
        userAiSettingsRepository.save(settings);
        return toDto(settings);
    }

    public UserNotificationSettingsDto getNotificationSettings() {
        User user = userService.getCurrentUser();
        UserNotificationSettings settings = userNotificationSettingsRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserNotificationSettings(user));
        return toDto(settings);
    }

    @Transactional
    public UserNotificationSettingsDto putNotificationSettings(UserNotificationSettingsDto request) {
        User user = userService.getCurrentUser();
        UserNotificationSettings settings = userNotificationSettingsRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserNotificationSettings(user));
        settings.updateSettings(request.notifyWorkoutStart(), request.notifyWeeklyGoal(), request.notifyLongAbsence(),
                request.notifyAiRecommend(), request.notifyBodyUpdate(), request.notifyWorkoutSummary(),
                request.notifyServiceEvent(), request.receiveChannel());
        userNotificationSettingsRepository.save(settings);
        return toDto(settings);
    }

    private UserPreferencesDto toDto(UserPreferences preferences) {
        return new UserPreferencesDto(
                toEnumList(preferences.getPreferredWorkoutTypes(), PreferredWorkoutType.class),
                toEnumList(preferences.getInjuryParts(), InjuryPart.class));
    }

    private UserAiSettingsDto toDto(UserAiSettings settings) {
        return new UserAiSettingsDto(
                settings.getRecommendationStyle(),
                settings.getExplanationLevel(),
                settings.getCoachTone(),
                settings.isAutoDailyRoutine(),
                settings.isAutoIntensityAdjust(),
                settings.isAutoWeakpartAlert(),
                settings.isAutoRestdaySuggest(),
                settings.isAutoPostureTip());
    }

    private UserNotificationSettingsDto toDto(UserNotificationSettings settings) {
        return new UserNotificationSettingsDto(
                settings.isNotifyWorkoutStart(),
                settings.isNotifyWeeklyGoal(),
                settings.isNotifyLongAbsence(),
                settings.isNotifyAiRecommend(),
                settings.isNotifyBodyUpdate(),
                settings.isNotifyWorkoutSummary(),
                settings.isNotifyServiceEvent(),
                settings.getReceiveChannel());
    }

    private List<String> toStringList(List<? extends Enum<?>> values) {
        return values == null ? List.of() : values.stream().map(Enum::name).toList();
    }

    private <T extends Enum<T>> List<T> toEnumList(List<String> values, Class<T> type) {
        return values == null ? List.of() : values.stream().map(v -> Enum.valueOf(type, v)).toList();
    }
}
