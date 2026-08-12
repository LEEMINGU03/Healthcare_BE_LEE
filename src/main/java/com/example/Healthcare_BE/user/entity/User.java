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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String email;

    private String phone;

    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "height_cm")
    private BigDecimal heightCm;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private List<String> goals = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExperienceLevel experienceLevel;

    @Column(name = "workout_frequency_per_week")
    private Integer workoutFrequencyPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_duration")
    private WorkoutDuration workoutDuration;

    @Column(name = "target_weight_kg")
    private BigDecimal targetWeightKg;

    @Column(name = "target_muscle_kg")
    private BigDecimal targetMuscleKg;

    @Column(name = "goal_target_date")
    private LocalDate goalTargetDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_workout")
    private WorkoutType previousWorkout;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public User(String name, Gender gender, BigDecimal heightCm, WorkoutType previousWorkout) {
        this.name = name;
        this.gender = gender;
        this.heightCm = heightCm;
        this.previousWorkout = previousWorkout;
    }

    /**
     * 소셜 로그인 최초 가입 시점 — 이름 외 프로필(성별·키 등)은 아직 없다.
     * 나머지는 프로필 완성 단계에서 채운다.
     */
    public User(String name) {
        this(name, null, null, null);
    }

    /**
     * 회원가입1(프로필 완성) 저장. name은 로그인 시 채워진 값을 유지하고 갱신하지 않는다.
     */
    public void updateProfile(Gender gender, BigDecimal heightCm, String nickname, LocalDate birthDate,
                               String email, String phone, String bio, String profileImageUrl,
                               List<String> goals, ExperienceLevel experienceLevel,
                               Integer workoutFrequencyPerWeek, WorkoutDuration workoutDuration,
                               BigDecimal targetWeightKg, BigDecimal targetMuscleKg, LocalDate goalTargetDate) {
        this.gender = gender;
        this.heightCm = heightCm;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.goals = goals != null ? goals : new ArrayList<>();
        this.experienceLevel = experienceLevel;
        this.workoutFrequencyPerWeek = workoutFrequencyPerWeek;
        this.workoutDuration = workoutDuration;
        this.targetWeightKg = targetWeightKg;
        this.targetMuscleKg = targetMuscleKg;
        this.goalTargetDate = goalTargetDate;
    }
}
