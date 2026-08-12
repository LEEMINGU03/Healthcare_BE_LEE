package com.example.Healthcare_BE.workout.repository;

import com.example.Healthcare_BE.workout.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {

    List<WorkoutLog> findByUserIdOrderByPerformedAtDesc(UUID userId);
}
