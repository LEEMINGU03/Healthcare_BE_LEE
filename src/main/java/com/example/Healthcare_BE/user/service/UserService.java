package com.example.Healthcare_BE.user.service;

import com.example.Healthcare_BE.auth.service.UnauthenticatedException;
import com.example.Healthcare_BE.user.dto.UserProfileDto;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * "현재 유저"는 JwtAuthenticationFilter가 Authorization 헤더의 Access Token을 검증해
 * SecurityContext에 세팅해준 유저 id로 조회한다 (principal = UUID).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UUID userId)) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UnauthenticatedException("유저를 찾을 수 없습니다: " + userId));
    }

    public UserProfileDto getProfile() {
        return toDto(getCurrentUser());
    }

    private UserProfileDto toDto(User user) {
        return new UserProfileDto(
                user.getName(),
                user.getGender(),
                user.getHeightCm(),
                user.getPreviousWorkout());
    }
}
