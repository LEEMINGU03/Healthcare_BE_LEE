package com.example.Healthcare_BE.workout.entity;

import com.example.Healthcare_BE.routine.entity.Routine;
import com.example.Healthcare_BE.routine.entity.RoutineExercise;
import com.example.Healthcare_BE.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 사용자의 운동 수행 기록. AI 루틴을 수행한 기록(routine != null)과 사용자 자유 입력
 * (routine == null) 둘 다 이 테이블 하나에 담는다 (database.md workout_logs 참고).
 */
@Entity
@Table(name = "workout_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * DB는 ON DELETE SET NULL(cascade 아님) — routines가 chat_messages에 cascade로 매달려
     * 있어, 채팅 세션이 지워져도 이 운동 이력은 남아야 하기 때문 (database.md 참고).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    /**
     * 어떤 운동을 수행했는지 특정하는 값. DB는 routine_id와 마찬가지로 ON DELETE SET NULL —
     * 루틴/세션이 지워져도 이 운동 이력은 남아야 한다 (database.md 참고).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id")
    private RoutineExercise routineExercise;

    @Column(name = "performed_at", nullable = false)
    private LocalDate performedAt;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group")
    private MuscleGroup muscleGroup;

    /** 추천 세트 수 스냅샷 — 나중에 routine 연결이 끊겨도(SET NULL) 완료율 계산에 쓸 수 있게 유지. */
    @Column(name = "planned_sets")
    private Integer plannedSets;

    @Column(name = "completed_sets")
    private Integer completedSets;

    private Integer reps;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public WorkoutLog(User user, Routine routine, RoutineExercise routineExercise, LocalDate performedAt,
                       String exerciseName, MuscleGroup muscleGroup, Integer plannedSets, Integer completedSets,
                       Integer reps, BigDecimal weightKg) {
        this.user = user;
        this.routine = routine;
        this.routineExercise = routineExercise;
        this.performedAt = performedAt;
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
        this.plannedSets = plannedSets;
        this.completedSets = completedSets;
        this.reps = reps;
        this.weightKg = weightKg;
    }
}
