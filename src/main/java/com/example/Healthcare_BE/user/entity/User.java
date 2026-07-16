package com.example.Healthcare_BE.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "height_cm", nullable = false)
    private BigDecimal heightCm;

    @Column(name = "target_gain_kg")
    private BigDecimal targetGainKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_workout")
    private WorkoutType previousWorkout;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public User(String name, Gender gender, BigDecimal heightCm,
                BigDecimal targetGainKg, WorkoutType previousWorkout) {
        this.name = name;
        this.gender = gender;
        this.heightCm = heightCm;
        this.targetGainKg = targetGainKg;
        this.previousWorkout = previousWorkout;
    }
}
