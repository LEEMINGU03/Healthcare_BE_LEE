package com.example.Healthcare_BE.user.service;

import com.example.Healthcare_BE.user.dto.UserMeResponse;
import com.example.Healthcare_BE.user.dto.UserProfileRequest;
import com.example.Healthcare_BE.user.entity.ExperienceLevel;
import com.example.Healthcare_BE.user.entity.Gender;
import com.example.Healthcare_BE.user.entity.Goal;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.entity.WorkoutDuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GET/PUT/PATCH /api/users/me (signup_profile_api_spec.md 2-1~2-3).
 */
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserMeResponse getMe() {
        return toMeResponse(userService.getCurrentUser());
    }

    /**
     * 회원가입1 통째 저장. name은 로그인 시 채워진 값을 유지한다(요청에 포함하지 않음).
     */
    @Transactional
    public UserMeResponse putMe(UserProfileRequest request) {
        User user = userService.getCurrentUser();
        user.updateProfile(
                request.gender(), request.heightCm(), request.nickname(), request.birthDate(),
                request.email(), request.phone(), request.bio(), request.profileImageUrl(),
                toStringList(request.goals()), request.experienceLevel(), request.workoutFrequencyPerWeek(),
                request.workoutDuration(), request.targetWeightKg(), request.targetMuscleKg(),
                request.goalTargetDate());
        return toMeResponse(user);
    }

    /**
     * 부분 수정. 요청 JSON에 키가 있는지 여부를 구분해야 하므로 Map으로 받는다(record는 "키 없음"과
     * "값이 null"을 구분하지 못한다). 키가 없는 필드는 현재 값을 그대로 유지하고, 키가 있고 값이 null이면
     * 비운다 — 미결사항 확정(값이 null이면 비우기). 병합한 전체 상태를 그대로 updateProfile에 위임한다.
     */
    @Transactional
    public UserMeResponse patchMe(Map<String, Object> patch) {
        User user = userService.getCurrentUser();

        Gender gender = patch.containsKey("gender")
                ? convert(patch.get("gender"), Gender.class) : user.getGender();
        BigDecimal heightCm = patch.containsKey("heightCm")
                ? convert(patch.get("heightCm"), BigDecimal.class) : user.getHeightCm();
        String nickname = patch.containsKey("nickname")
                ? convert(patch.get("nickname"), String.class) : user.getNickname();
        LocalDate birthDate = patch.containsKey("birthDate")
                ? convert(patch.get("birthDate"), LocalDate.class) : user.getBirthDate();
        String email = patch.containsKey("email")
                ? convert(patch.get("email"), String.class) : user.getEmail();
        String phone = patch.containsKey("phone")
                ? convert(patch.get("phone"), String.class) : user.getPhone();
        String bio = patch.containsKey("bio")
                ? convert(patch.get("bio"), String.class) : user.getBio();
        String profileImageUrl = patch.containsKey("profileImageUrl")
                ? convert(patch.get("profileImageUrl"), String.class) : user.getProfileImageUrl();
        List<String> goals = patch.containsKey("goals")
                ? toStringList(convertList(patch.get("goals"), Goal.class)) : user.getGoals();
        ExperienceLevel experienceLevel = patch.containsKey("experienceLevel")
                ? convert(patch.get("experienceLevel"), ExperienceLevel.class) : user.getExperienceLevel();
        Integer workoutFrequencyPerWeek = patch.containsKey("workoutFrequencyPerWeek")
                ? convert(patch.get("workoutFrequencyPerWeek"), Integer.class) : user.getWorkoutFrequencyPerWeek();
        WorkoutDuration workoutDuration = patch.containsKey("workoutDuration")
                ? convert(patch.get("workoutDuration"), WorkoutDuration.class) : user.getWorkoutDuration();
        BigDecimal targetWeightKg = patch.containsKey("targetWeightKg")
                ? convert(patch.get("targetWeightKg"), BigDecimal.class) : user.getTargetWeightKg();
        BigDecimal targetMuscleKg = patch.containsKey("targetMuscleKg")
                ? convert(patch.get("targetMuscleKg"), BigDecimal.class) : user.getTargetMuscleKg();
        LocalDate goalTargetDate = patch.containsKey("goalTargetDate")
                ? convert(patch.get("goalTargetDate"), LocalDate.class) : user.getGoalTargetDate();

        if (heightCm != null && heightCm.signum() <= 0) {
            throw new InvalidProfileFieldException("heightCm은 0보다 커야 합니다.");
        }
        if (workoutFrequencyPerWeek != null && (workoutFrequencyPerWeek < 1 || workoutFrequencyPerWeek > 7)) {
            throw new InvalidProfileFieldException("workoutFrequencyPerWeek은 1~7 사이여야 합니다.");
        }

        user.updateProfile(gender, heightCm, nickname, birthDate, email, phone, bio, profileImageUrl,
                goals, experienceLevel, workoutFrequencyPerWeek, workoutDuration,
                targetWeightKg, targetMuscleKg, goalTargetDate);
        return toMeResponse(user);
    }

    private <T> T convert(Object rawValue, Class<T> type) {
        if (rawValue == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(rawValue, type);
        } catch (RuntimeException e) {
            throw new InvalidProfileFieldException("요청 값이 올바르지 않습니다: " + rawValue);
        }
    }

    private <T> List<T> convertList(Object rawValue, Class<T> elementType) {
        if (rawValue == null) {
            return null;
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return objectMapper.convertValue(rawValue, listType);
        } catch (RuntimeException e) {
            throw new InvalidProfileFieldException("요청 값이 올바르지 않습니다: " + rawValue);
        }
    }

    private List<String> toStringList(List<Goal> goals) {
        return goals == null ? null : goals.stream().map(Enum::name).toList();
    }

    private UserMeResponse toMeResponse(User user) {
        boolean profileCompleted = user.getGender() != null && user.getHeightCm() != null;
        List<Goal> goals = user.getGoals() == null ? List.of()
                : user.getGoals().stream().map(Goal::valueOf).toList();
        return new UserMeResponse(
                profileCompleted,
                user.getName(),
                user.getNickname(),
                user.getGender(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPhone(),
                user.getBio(),
                user.getProfileImageUrl(),
                user.getHeightCm(),
                goals,
                user.getExperienceLevel(),
                user.getWorkoutFrequencyPerWeek(),
                user.getWorkoutDuration(),
                user.getTargetWeightKg(),
                user.getTargetMuscleKg(),
                user.getGoalTargetDate());
    }
}
