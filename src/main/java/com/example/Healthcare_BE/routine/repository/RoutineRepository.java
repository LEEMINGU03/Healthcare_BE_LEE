package com.example.Healthcare_BE.routine.repository;

import com.example.Healthcare_BE.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoutineRepository extends JpaRepository<Routine, UUID> {

    Optional<Routine> findByChatMessageId(UUID chatMessageId);
}
