package com.example.Healthcare_BE.routine.repository;

import com.example.Healthcare_BE.routine.entity.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, UUID> {
}
