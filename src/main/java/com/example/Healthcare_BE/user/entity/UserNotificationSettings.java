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
 * 프로필 "알림 설정". 유저 1:1.
 */
@Entity
@Table(name = "user_notification_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "notify_workout_start", nullable = false)
    private boolean notifyWorkoutStart = true;

    @Column(name = "notify_weekly_goal", nullable = false)
    private boolean notifyWeeklyGoal = true;

    @Column(name = "notify_long_absence", nullable = false)
    private boolean notifyLongAbsence = true;

    @Column(name = "notify_ai_recommend", nullable = false)
    private boolean notifyAiRecommend = false;

    @Column(name = "notify_body_update", nullable = false)
    private boolean notifyBodyUpdate = true;

    @Column(name = "notify_workout_summary", nullable = false)
    private boolean notifyWorkoutSummary = true;

    @Column(name = "notify_service_event", nullable = false)
    private boolean notifyServiceEvent = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "receive_channel", nullable = false)
    private ReceiveChannel receiveChannel = ReceiveChannel.APP;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public UserNotificationSettings(User user) {
        this.user = user;
    }

    public void updateSettings(boolean notifyWorkoutStart, boolean notifyWeeklyGoal, boolean notifyLongAbsence,
                                boolean notifyAiRecommend, boolean notifyBodyUpdate, boolean notifyWorkoutSummary,
                                boolean notifyServiceEvent, ReceiveChannel receiveChannel) {
        this.notifyWorkoutStart = notifyWorkoutStart;
        this.notifyWeeklyGoal = notifyWeeklyGoal;
        this.notifyLongAbsence = notifyLongAbsence;
        this.notifyAiRecommend = notifyAiRecommend;
        this.notifyBodyUpdate = notifyBodyUpdate;
        this.notifyWorkoutSummary = notifyWorkoutSummary;
        this.notifyServiceEvent = notifyServiceEvent;
        this.receiveChannel = receiveChannel != null ? receiveChannel : ReceiveChannel.APP;
        this.updatedAt = OffsetDateTime.now();
    }
}
