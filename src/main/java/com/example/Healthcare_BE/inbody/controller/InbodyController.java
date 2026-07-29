package com.example.Healthcare_BE.inbody.controller;

import com.example.Healthcare_BE.inbody.dto.InbodyCreateRequest;
import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.inbody.service.InbodyService;
import com.example.Healthcare_BE.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * api.md 3.1, signup_profile_api_spec.md 2-4.
 */
@RestController
@RequestMapping("/api/inbody")
@RequiredArgsConstructor
public class InbodyController {

    private final InbodyService inbodyService;
    private final UserService userService;

    @GetMapping("/recent")
    public InbodyRecentResponse getRecent() {
        return inbodyService.getRecent(userService.getCurrentUser().getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InbodyRecentResponse create(@Valid @RequestBody InbodyCreateRequest request) {
        return inbodyService.create(userService.getCurrentUser(), request);
    }
}
