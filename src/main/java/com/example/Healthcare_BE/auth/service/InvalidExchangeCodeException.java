package com.example.Healthcare_BE.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * POST /api/auth/exchange에서 code가 없거나, 만료됐거나, 이미 소비된 경우의 401 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class InvalidExchangeCodeException extends ErrorResponseException {

    public InvalidExchangeCodeException(String detail) {
        super(HttpStatus.UNAUTHORIZED);
        setDetail(detail);
    }
}
