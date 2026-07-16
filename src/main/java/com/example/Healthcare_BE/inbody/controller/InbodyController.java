package com.example.Healthcare_BE.inbody.controller;

import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.inbody.service.InbodyService;
import com.example.Healthcare_BE.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * api.md 3.1.
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
}
