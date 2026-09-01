package com.example.Healthcare_BE.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
