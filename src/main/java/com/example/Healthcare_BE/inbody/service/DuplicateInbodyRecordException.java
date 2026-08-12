package com.example.Healthcare_BE.inbody.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * POST /api/inbody에서 같은 유저·같은 날짜(measured_at) 인바디가 이미 있는 경우의 409 케이스
 * (signup_profile_api_spec.md 4장 미결사항 확정: 같은 날 재저장은 409). ErrorResponseException 상속 이유는
 * InbodyRecordNotFoundException 주석 참고.
 */
public class DuplicateInbodyRecordException extends ErrorResponseException {

    public DuplicateInbodyRecordException(String detail) {
        super(HttpStatus.CONFLICT);
        setDetail(detail);
    }
}
