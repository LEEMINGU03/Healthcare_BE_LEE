package com.example.Healthcare_BE.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * PATCH /api/users/me에서 요청 Map 값을 타입 변환/범위 검증하는 과정에서 실패한 경우의 400 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class InvalidProfileFieldException extends ErrorResponseException {

    public InvalidProfileFieldException(String detail) {
        super(HttpStatus.BAD_REQUEST);
        setDetail(detail);
    }
}
