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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutineService {

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
                    exercise.imageUrl()));
        }
        routineRepository.save(routine);
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
                        exercise.getImageUrl()))
                .toList();
        return new RoutineDto(routine.getTitle(), exercises);
    }
}
