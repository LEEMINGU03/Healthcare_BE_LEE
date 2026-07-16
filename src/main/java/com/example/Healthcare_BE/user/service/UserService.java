package com.example.Healthcare_BE.user.service;

import com.example.Healthcare_BE.user.dto.UserProfileDto;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * MVP는 로그인이 없어 고정 더미 유저 1명을 "현재 유저"로 취급한다.
 * 인증 도입 시 getCurrentUser()만 JWT 기반 조회로 교체하면 된다 (stack-architecture 메모리 참고).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${app.mvp.dummy-user-id}")
    private String dummyUserId;

    public User getCurrentUser() {
        UUID id = UUID.fromString(dummyUserId);
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("더미 유저가 존재하지 않습니다: " + id));
    }

    public UserProfileDto getProfile() {
        return toDto(getCurrentUser());
    }

    private UserProfileDto toDto(User user) {
        return new UserProfileDto(
                user.getName(),
                user.getGender(),
                user.getHeightCm(),
                user.getTargetGainKg(),
                user.getPreviousWorkout());
    }
}
