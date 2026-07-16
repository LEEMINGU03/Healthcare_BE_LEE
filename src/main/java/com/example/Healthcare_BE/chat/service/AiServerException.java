package com.example.Healthcare_BE.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * AI 서버 호출 실패·타임아웃·응답 파싱 실패 (api.md 3.2의 502 케이스).
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class AiServerException extends ErrorResponseException {

    public AiServerException(String detail, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, cause);
        setDetail(detail);
    }
}
