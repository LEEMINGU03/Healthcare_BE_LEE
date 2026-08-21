package com.example.Healthcare_BE.inbody.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * GET /api/inbody/recent의 404 케이스 (api.md 3.1). ErrorResponseException을 상속해야
 * spring.mvc.problemdetails.enabled=true가 실제로 RFC 7807 ProblemDetail로 직렬화한다 —
 * @ResponseStatus는 response.sendError()로 빠져 Boot 기본 에러 바디가 나가버린다.
 */
public class InbodyRecordNotFoundException extends ErrorResponseException {

    public InbodyRecordNotFoundException(String detail) {
        super(HttpStatus.NOT_FOUND);
        setDetail(detail);
    }
}
