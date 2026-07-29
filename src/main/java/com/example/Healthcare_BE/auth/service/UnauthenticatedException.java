package com.example.Healthcare_BE.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * 유효한 Access Token 없이 로그인이 필요한 동작을 시도한 경우의 401 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class UnauthenticatedException extends ErrorResponseException {

    public UnauthenticatedException(String detail) {
        super(HttpStatus.UNAUTHORIZED);
        setDetail(detail);
    }
}
