package com.example.Healthcare_BE.auth.service;

import com.example.Healthcare_BE.auth.entity.RefreshToken;
import com.example.Healthcare_BE.auth.repository.RefreshTokenRepository;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Refresh Token은 JWT다(서명 검증만으로 만료 여부까지 확인 가능). 다만 로그아웃/연동 해제 시
 * 즉시 무효화가 가능하도록, 발급한 토큰 문자열을 DB에도 그대로 저장해둔다 — 검증 시 서명뿐 아니라
 * DB에 그 행이 아직 있는지도 함께 확인한다(하이브리드 방식).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${app.jwt.refresh-token-expiry-days}")
    private long refreshTokenExpiryDays;

    /**
     * userId는 방금 로그인/생성이 끝난 유저라 존재가 보장되므로, 조회 대신 참조(proxy)만 얻는다.
     */
    public String issue(UUID userId) {
        String token = jwtProvider.createRefreshToken(userId);
        User user = userRepository.getReferenceById(userId);
        OffsetDateTime expiresAt = OffsetDateTime.now().plusDays(refreshTokenExpiryDays);
        refreshTokenRepository.save(new RefreshToken(user, token, expiresAt));
        return token;
    }

    public User validate(String token) {
        try {
            jwtProvider.validateAndGetUserId(token);
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException("유효하지 않거나 만료된 refresh token입니다.");
        }
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("이미 로그아웃되었거나 존재하지 않는 refresh token입니다."));
        return refreshToken.getUser();
    }
}
