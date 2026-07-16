package com.example.Healthcare_BE.mealplan;

import com.example.Healthcare_BE.chat.entity.ChatMessage;
import com.example.Healthcare_BE.chat.repository.ChatMessageRepository;
import com.example.Healthcare_BE.chat.entity.ChatRole;
import com.example.Healthcare_BE.chat.entity.ChatSession;
import com.example.Healthcare_BE.chat.repository.ChatSessionRepository;
import com.example.Healthcare_BE.chat.entity.ChatType;
import com.example.Healthcare_BE.mealplan.entity.*;
import com.example.Healthcare_BE.mealplan.repository.MealPlanRepository;
import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MealPlanPersistenceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Test
    void 식단표와_요일별_끼니가_함께_저장되고_조회된다() {
        User user = userRepository.save(
                new User("테스트유저", Gender.FEMALE, BigDecimal.valueOf(160.0), null, null));
        ChatSession session = chatSessionRepository.save(
                new ChatSession(user, ChatType.NUTRITION, "테스트 세션"));
        ChatMessage message = chatMessageRepository.save(
                new ChatMessage(session, ChatRole.ASSISTANT, "말씀하신대로 식단표를 추천하여 제작하겠습니다."));

        MealPlan mealPlan = new MealPlan(message, "NUTRITION AI 식단표");
        MealPlanDay monday = new MealPlanDay(MealPlanDayOfWeek.MON);
        monday.addMeal(new MealPlanMeal(MealSlot.BREAKFAST, "현미밥, 닭가슴살, 나물",
                500, BigDecimal.valueOf(60), BigDecimal.valueOf(30), BigDecimal.valueOf(15)));
        monday.addMeal(new MealPlanMeal(MealSlot.LUNCH, "잡곡밥, 소고기 야채볶음",
                650, BigDecimal.valueOf(70), BigDecimal.valueOf(35), BigDecimal.valueOf(18)));
        mealPlan.addDay(monday);
        mealPlanRepository.save(mealPlan);

        Optional<MealPlan> found = mealPlanRepository.findByChatMessageId(message.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("NUTRITION AI 식단표");
        assertThat(found.get().getDays()).hasSize(1);

        MealPlanDay foundDay = found.get().getDays().get(0);
        assertThat(foundDay.getDayOfWeek()).isEqualTo(MealPlanDayOfWeek.MON);
        assertThat(foundDay.getMeals()).hasSize(2);
        assertThat(foundDay.getMeals().get(0).getMenu()).isEqualTo("현미밥, 닭가슴살, 나물");
    }
}
