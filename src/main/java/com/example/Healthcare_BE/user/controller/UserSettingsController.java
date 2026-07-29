package com.example.Healthcare_BE.user.controller;

import com.example.Healthcare_BE.user.dto.UserAiSettingsDto;
import com.example.Healthcare_BE.user.dto.UserNotificationSettingsDto;
import com.example.Healthcare_BE.user.dto.UserPreferencesDto;
import com.example.Healthcare_BE.user.service.UserSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설정 3종 GET·PUT (signup_profile_api_spec.md 2-5~2-7).
 */
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping("/preferences")
    public UserPreferencesDto getPreferences() {
        return userSettingsService.getPreferences();
    }

    @PutMapping("/preferences")
    public UserPreferencesDto putPreferences(@Valid @RequestBody UserPreferencesDto request) {
        return userSettingsService.putPreferences(request);
    }

    @GetMapping("/ai-settings")
    public UserAiSettingsDto getAiSettings() {
        return userSettingsService.getAiSettings();
    }

    @PutMapping("/ai-settings")
    public UserAiSettingsDto putAiSettings(@Valid @RequestBody UserAiSettingsDto request) {
        return userSettingsService.putAiSettings(request);
    }

    @GetMapping("/notification-settings")
    public UserNotificationSettingsDto getNotificationSettings() {
        return userSettingsService.getNotificationSettings();
    }

    @PutMapping("/notification-settings")
    public UserNotificationSettingsDto putNotificationSettings(@Valid @RequestBody UserNotificationSettingsDto request) {
        return userSettingsService.putNotificationSettings(request);
    }
}
