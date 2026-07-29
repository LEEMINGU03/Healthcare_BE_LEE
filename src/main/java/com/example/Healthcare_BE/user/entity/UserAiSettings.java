package com.example.Healthcare_BE.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 프로필 "AI 맞춤 설정". 유저 1:1.
 */
@Entity
@Table(name = "user_ai_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAiSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_style")
    private RecommendationStyle recommendationStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "explanation_level")
    private ExplanationLevel explanationLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "coach_tone")
    private CoachTone coachTone;

    @Column(name = "auto_daily_routine", nullable = false)
    private boolean autoDailyRoutine = true;

    @Column(name = "auto_intensity_adjust", nullable = false)
    private boolean autoIntensityAdjust = true;

    @Column(name = "auto_weakpart_alert", nullable = false)
    private boolean autoWeakpartAlert = true;

    @Column(name = "auto_restday_suggest", nullable = false)
    private boolean autoRestdaySuggest = false;

    @Column(name = "auto_posture_tip", nullable = false)
    private boolean autoPostureTip = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public UserAiSettings(User user) {
        this.user = user;
    }

    public void updateSettings(RecommendationStyle recommendationStyle, ExplanationLevel explanationLevel,
                                CoachTone coachTone, boolean autoDailyRoutine, boolean autoIntensityAdjust,
                                boolean autoWeakpartAlert, boolean autoRestdaySuggest, boolean autoPostureTip) {
        this.recommendationStyle = recommendationStyle;
        this.explanationLevel = explanationLevel;
        this.coachTone = coachTone;
        this.autoDailyRoutine = autoDailyRoutine;
        this.autoIntensityAdjust = autoIntensityAdjust;
        this.autoWeakpartAlert = autoWeakpartAlert;
        this.autoRestdaySuggest = autoRestdaySuggest;
        this.autoPostureTip = autoPostureTip;
        this.updatedAt = OffsetDateTime.now();
    }
}
