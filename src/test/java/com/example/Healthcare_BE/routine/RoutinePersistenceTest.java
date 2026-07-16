package com.example.Healthcare_BE.routine;

import com.example.Healthcare_BE.chat.entity.ChatMessage;
import com.example.Healthcare_BE.chat.repository.ChatMessageRepository;
import com.example.Healthcare_BE.chat.entity.ChatRole;
import com.example.Healthcare_BE.chat.entity.ChatSession;
import com.example.Healthcare_BE.chat.repository.ChatSessionRepository;
import com.example.Healthcare_BE.chat.entity.ChatType;
import com.example.Healthcare_BE.routine.entity.Routine;
import com.example.Healthcare_BE.routine.entity.RoutineExercise;
import com.example.Healthcare_BE.routine.repository.RoutineRepository;
import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoutinePersistenceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private RoutineRepository routineRepository;

    @Test
    void 루틴과_운동_항목이_함께_저장되고_조회된다() {
        User user = userRepository.save(
                new User("테스트유저", Gender.MALE, BigDecimal.valueOf(175.0), BigDecimal.valueOf(3.0), null));
        ChatSession session = chatSessionRepository.save(
                new ChatSession(user, ChatType.COACHING, "테스트 세션"));
        ChatMessage message = chatMessageRepository.save(
                new ChatMessage(session, ChatRole.ASSISTANT, "말씀하신대로 상체루틴을 추천하여 제작하겠습니다."));

        Routine routine = new Routine(message, "COACHING AI 운동루틴");
        routine.addExercise(new RoutineExercise(1, "등업", "3~4세트", "8~12회",
                "어깨너비보다 약간 넓게 바를 잡는다.", null));
        routine.addExercise(new RoutineExercise(2, "벤치프레스", "3~4세트", "8~12회",
                "가슴 중앙까지 바를 내린다.", null));
        routineRepository.save(routine);

        Optional<Routine> found = routineRepository.findByChatMessageId(message.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("COACHING AI 운동루틴");

        List<RoutineExercise> exercises = found.get().getExercises();
        assertThat(exercises).hasSize(2);
        assertThat(exercises.get(0).getOrderNo()).isEqualTo(1);
        assertThat(exercises.get(0).getName()).isEqualTo("등업");
        assertThat(exercises.get(1).getOrderNo()).isEqualTo(2);
    }
}
