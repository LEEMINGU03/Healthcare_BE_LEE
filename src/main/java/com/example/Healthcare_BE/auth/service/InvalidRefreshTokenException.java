package com.example.Healthcare_BE.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * POST /api/auth/refresh에서 refresh token이 없거나 만료된 경우의 401 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class InvalidRefreshTokenException extends ErrorResponseException {

    public InvalidRefreshTokenException(String detail) {
        super(HttpStatus.UNAUTHORIZED);
        setDetail(detail);
    }
}
