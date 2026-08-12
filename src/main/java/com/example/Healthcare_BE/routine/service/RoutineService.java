package com.example.Healthcare_BE.routine.service;

import com.example.Healthcare_BE.chat.entity.ChatMessage;
import com.example.Healthcare_BE.routine.dto.RoutineDto;
import com.example.Healthcare_BE.routine.dto.RoutineExerciseDto;
import com.example.Healthcare_BE.routine.entity.Routine;
import com.example.Healthcare_BE.routine.entity.RoutineExercise;
import com.example.Healthcare_BE.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {

    /**
     * routine_exercises.body_part의 DB CHECK와 1:1 (database.md 참고). AI가 이 밖의 값을
     * 보내면 그대로 저장 시도할 경우 CHECK 위반으로 채팅 요청 전체가 깨지므로, 여기서 먼저
     * 걸러 null로 저장한다 — "부위 없음"과 동일하게 취급.
     */
    private static final Set<String> VALID_BODY_PARTS = Set.of(
            "BACK", "CHEST", "BICEPS", "TRICEPS", "SHOULDER", "CORE", "GLUTES", "THIGH", "CALF");

    private final RoutineRepository routineRepository;

    /** AI 응답의 result.routine을 정규화된 테이블에 분해해 저장한다 (database.md 5장). */
    public void saveRoutine(ChatMessage chatMessage, RoutineDto dto) {
        Routine routine = new Routine(chatMessage, dto.title());
        for (RoutineExerciseDto exercise : dto.exercises()) {
            routine.addExercise(new RoutineExercise(
                    exercise.order(),
                    exercise.name(),
                    exercise.sets(),
                    exercise.reps(),
                    exercise.description(),
                    exercise.imageUrl(),
                    sanitizeBodyPart(exercise.bodyPart())));
        }
        routineRepository.save(routine);
    }

    private String sanitizeBodyPart(String bodyPart) {
        if (bodyPart == null) {
            return null;
        }
        String normalized = bodyPart.strip().toUpperCase();
        return VALID_BODY_PARTS.contains(normalized) ? normalized : null;
    }

    /** 세션 상세 조회 시 저장된 테이블을 다시 JSON 모양으로 조립한다 (api.md 3.4). */
    public Optional<RoutineDto> findByChatMessage(UUID chatMessageId) {
        return routineRepository.findByChatMessageId(chatMessageId).map(this::toDto);
    }

    private RoutineDto toDto(Routine routine) {
        List<RoutineExerciseDto> exercises = routine.getExercises().stream()
                .map(exercise -> new RoutineExerciseDto(
                        exercise.getOrderNo(),
                        exercise.getName(),
                        exercise.getSets(),
                        exercise.getReps(),
                        exercise.getDescription(),
                        exercise.getImageUrl(),
                        exercise.getBodyPart()))
                .toList();
        return new RoutineDto(routine.getTitle(), exercises);
    }
}
