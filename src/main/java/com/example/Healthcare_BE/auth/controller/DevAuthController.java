package com.example.Healthcare_BE.auth.controller;

import com.example.Healthcare_BE.auth.dto.AccessTokenResponse;
import com.example.Healthcare_BE.auth.service.JwtProvider;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로컬 개발 전용 — 실제 구글 로그인 없이 고정 테스트 유저("테스트유저")로 Access Token을 즉시 발급한다.
 * local 프로파일에서만 빈이 뜬다(운영에는 노출되지 않음). 회원가입/프로필 API를 프론트 없이 붙여볼 때 사용.
 */
@Profile("local")
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevAuthController {

    private static final String DEV_USER_NAME = "테스트유저";

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public AccessTokenResponse login() {
        User user = userRepository.findByName(DEV_USER_NAME)
                .orElseGet(() -> userRepository.save(new User(DEV_USER_NAME)));
        String accessToken = jwtProvider.createAccessToken(user.getId());
        return new AccessTokenResponse(accessToken);
    }
}
