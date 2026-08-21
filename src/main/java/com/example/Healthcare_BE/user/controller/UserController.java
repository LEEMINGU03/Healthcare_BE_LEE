package com.example.Healthcare_BE.user.controller;

import com.example.Healthcare_BE.user.dto.UserMeResponse;
import com.example.Healthcare_BE.user.dto.UserProfileRequest;
import com.example.Healthcare_BE.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * signup_profile_api_spec.md 2-1~2-3.
 */
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserMeResponse getMe() {
        return userProfileService.getMe();
    }

    @PutMapping
    public UserMeResponse putMe(@Valid @RequestBody UserProfileRequest request) {
        return userProfileService.putMe(request);
    }

    @PatchMapping
    public UserMeResponse patchMe(@RequestBody Map<String, Object> patch) {
        return userProfileService.patchMe(patch);
    }
}
