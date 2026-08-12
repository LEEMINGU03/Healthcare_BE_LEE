package com.example.Healthcare_BE.workout.service;

import com.example.Healthcare_BE.routine.entity.Routine;
import com.example.Healthcare_BE.routine.entity.RoutineExercise;
import com.example.Healthcare_BE.routine.repository.RoutineExerciseRepository;
import com.example.Healthcare_BE.routine.repository.RoutineRepository;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.workout.dto.WorkoutLogCreateRequest;
import com.example.Healthcare_BE.workout.dto.WorkoutLogResponse;
import com.example.Healthcare_BE.workout.dto.WorkoutLogStatus;
import com.example.Healthcare_BE.workout.entity.MuscleGroup;
import com.example.Healthcare_BE.workout.entity.WorkoutLog;
import com.example.Healthcare_BE.workout.repository.WorkoutLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutLogService {

    /** 완료율 임계값 — 이 값 이상이면 COMPLETED, 미만이면 INCOMPLETE (조회 시점에 계산). */
    private static final BigDecimal COMPLETION_THRESHOLD_RATE = new BigDecimal("0.8");

    /**
     * AI 부위 원본(9개, routine_exercises.body_part)을 workout_logs.muscle_group(7개)으로
     * 매핑한다. 여기 없는 키(예: 예상 밖의 값)는 Map.get()이 null을 반환해 muscle_group도
     * null이 된다 — 매핑 실패로 저장 자체가 깨지면 안 되기 때문.
     */
    private static final Map<String, MuscleGroup> BODY_PART_TO_MUSCLE_GROUP = Map.ofEntries(
            Map.entry("BACK", MuscleGroup.BACK),
            Map.entry("CHEST", MuscleGroup.CHEST),
            Map.entry("SHOULDER", MuscleGroup.SHOULDER),
            Map.entry("CORE", MuscleGroup.CORE),
            Map.entry("BICEPS", MuscleGroup.ARM),
            Map.entry("TRICEPS", MuscleGroup.ARM),
            Map.entry("GLUTES", MuscleGroup.LOWER_BODY),
            Map.entry("THIGH", MuscleGroup.LOWER_BODY),
            Map.entry("CALF", MuscleGroup.LOWER_BODY));

    private final WorkoutLogRepository workoutLogRepository;
    private final RoutineRepository routineRepository;
    private final RoutineExerciseRepository routineExerciseRepository;

    /**
     * POST /api/workout-logs.
     * routineExerciseId가 있으면 그 운동의 body_part로 muscle_group을 백엔드가 계산해서
     * 채운다(요청의 muscleGroup은 무시). 없으면 자유 입력으로 보고 요청의 muscleGroup을
     * 그대로 쓴다. routineId만 있고 routineExerciseId가 없으면(하위 호환) 루틴에만 연결하고
     * muscle_group 자동 계산은 하지 않는다.
     */
    public WorkoutLogResponse create(User user, WorkoutLogCreateRequest request) {
        Routine routine = null;
        RoutineExercise routineExercise = null;
        MuscleGroup muscleGroup = request.muscleGroup();

        if (request.routineExerciseId() != null) {
            routineExercise = routineExerciseRepository.findById(request.routineExerciseId())
                    .orElseThrow(() -> new RoutineNotFoundException(
                            "루틴 운동 항목을 찾을 수 없습니다: " + request.routineExerciseId()));
            routine = routineExercise.getRoutine();
            if (!routine.getChatMessage().getSession().getUser().getId().equals(user.getId())) {
                throw new InvalidWorkoutLogRequestException("routineExerciseId가 다른 유저의 루틴을 가리킵니다.");
            }
            muscleGroup = BODY_PART_TO_MUSCLE_GROUP.get(routineExercise.getBodyPart());
        } else if (request.routineId() != null) {
            routine = routineRepository.findById(request.routineId())
                    .orElseThrow(() -> new RoutineNotFoundException("루틴을 찾을 수 없습니다: " + request.routineId()));
            if (!routine.getChatMessage().getSession().getUser().getId().equals(user.getId())) {
                throw new InvalidWorkoutLogRequestException("routineId가 다른 유저의 루틴을 가리킵니다.");
            }
        }

        WorkoutLog workoutLog = new WorkoutLog(user, routine, routineExercise, request.performedAt(),
                request.exerciseName(), muscleGroup, request.plannedSets(), request.completedSets(),
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
                workoutLog.getRoutineExercise() != null ? workoutLog.getRoutineExercise().getId() : null,
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
