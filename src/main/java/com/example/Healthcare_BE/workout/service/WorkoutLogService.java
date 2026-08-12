package com.example.Healthcare_BE.workout.service;

import com.example.Healthcare_BE.routine.entity.Routine;
import com.example.Healthcare_BE.routine.repository.RoutineRepository;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.workout.dto.WorkoutLogCreateRequest;
import com.example.Healthcare_BE.workout.dto.WorkoutLogResponse;
import com.example.Healthcare_BE.workout.dto.WorkoutLogStatus;
import com.example.Healthcare_BE.workout.entity.WorkoutLog;
import com.example.Healthcare_BE.workout.repository.WorkoutLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutLogService {

    /** 완료율 임계값 — 이 값 이상이면 COMPLETED, 미만이면 INCOMPLETE (조회 시점에 계산). */
    private static final BigDecimal COMPLETION_THRESHOLD_RATE = new BigDecimal("0.8");

    private final WorkoutLogRepository workoutLogRepository;
    private final RoutineRepository routineRepository;

    /** POST /api/workout-logs — routineId가 있으면 AI 루틴 수행, 없으면 자유 입력. */
    public WorkoutLogResponse create(User user, WorkoutLogCreateRequest request) {
        Routine routine = null;
        if (request.routineId() != null) {
            routine = routineRepository.findById(request.routineId())
                    .orElseThrow(() -> new RoutineNotFoundException("루틴을 찾을 수 없습니다: " + request.routineId()));
            if (!routine.getChatMessage().getSession().getUser().getId().equals(user.getId())) {
                throw new InvalidWorkoutLogRequestException("routineId가 다른 유저의 루틴을 가리킵니다.");
            }
        }

        WorkoutLog workoutLog = new WorkoutLog(user, routine, request.performedAt(), request.exerciseName(),
                request.muscleGroup(), request.plannedSets(), request.completedSets(),
                request.reps(), request.weightKg());
        workoutLogRepository.save(workoutLog);
        return toDto(workoutLog);
    }

    /** GET /api/workout-logs — 로그인 유저의 수행 기록 전체, performedAt 내림차순. */
    public List<WorkoutLogResponse> getHistory(UUID userId) {
        return workoutLogRepository.findByUserIdOrderByPerformedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    private WorkoutLogResponse toDto(WorkoutLog workoutLog) {
        BigDecimal completionRate = computeCompletionRate(workoutLog.getPlannedSets(), workoutLog.getCompletedSets());
        return new WorkoutLogResponse(
                workoutLog.getId(),
                workoutLog.getRoutine() != null ? workoutLog.getRoutine().getId() : null,
                workoutLog.getPerformedAt(),
                workoutLog.getExerciseName(),
                workoutLog.getMuscleGroup(),
                workoutLog.getPlannedSets(),
                workoutLog.getCompletedSets(),
                workoutLog.getReps(),
                workoutLog.getWeightKg(),
                completionRate,
                computeStatus(completionRate));
    }

    /**
     * plannedSets가 없거나 0이면 자유 입력으로 보고 100%(완료)로 간주한다 —
     * 비교할 계획값이 없는 기록을 미완료로 취급하지 않기 위함.
     */
    private BigDecimal computeCompletionRate(Integer plannedSets, Integer completedSets) {
        if (plannedSets == null || plannedSets == 0) {
            return BigDecimal.ONE;
        }
        int completed = completedSets != null ? completedSets : 0;
        return BigDecimal.valueOf(completed)
                .divide(BigDecimal.valueOf(plannedSets), 4, RoundingMode.HALF_UP);
    }

    private WorkoutLogStatus computeStatus(BigDecimal completionRate) {
        return completionRate.compareTo(COMPLETION_THRESHOLD_RATE) >= 0
                ? WorkoutLogStatus.COMPLETED
                : WorkoutLogStatus.INCOMPLETE;
    }
}
