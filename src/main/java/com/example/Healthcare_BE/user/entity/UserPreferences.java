package com.example.Healthcare_BE.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 프로필 "운동 선호 설정" — 순수 선호(다중선택). 유저 1:1.
 */
@Entity
@Table(name = "user_preferences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_workout_types", columnDefinition = "text[]", nullable = false)
    private List<String> preferredWorkoutTypes = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "injury_parts", columnDefinition = "text[]", nullable = false)
    private List<String> injuryParts = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public UserPreferences(User user) {
        this.user = user;
    }

    public void updatePreferences(List<String> preferredWorkoutTypes, List<String> injuryParts) {
        this.preferredWorkoutTypes = preferredWorkoutTypes != null ? preferredWorkoutTypes : new ArrayList<>();
        this.injuryParts = injuryParts != null ? injuryParts : new ArrayList<>();
        this.updatedAt = OffsetDateTime.now();
    }
}
