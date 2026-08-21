package com.example.Healthcare_BE.workout.controller;

import com.example.Healthcare_BE.user.service.UserService;
import com.example.Healthcare_BE.workout.dto.WorkoutLogCreateRequest;
import com.example.Healthcare_BE.workout.dto.WorkoutLogResponse;
import com.example.Healthcare_BE.workout.service.WorkoutLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * api.md 3.5, 3.6.
 */
@RestController
@RequestMapping("/api/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutLogResponse create(@Valid @RequestBody WorkoutLogCreateRequest request) {
        return workoutLogService.create(userService.getCurrentUser(), request);
    }

    @GetMapping
    public List<WorkoutLogResponse> getHistory() {
        return workoutLogService.getHistory(userService.getCurrentUser().getId());
    }
}
