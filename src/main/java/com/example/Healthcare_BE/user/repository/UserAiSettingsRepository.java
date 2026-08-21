package com.example.Healthcare_BE.user.repository;

import com.example.Healthcare_BE.user.entity.UserAiSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAiSettingsRepository extends JpaRepository<UserAiSettings, UUID> {

    Optional<UserAiSettings> findByUserId(UUID userId);
}
