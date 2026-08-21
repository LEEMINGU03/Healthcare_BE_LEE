package com.example.Healthcare_BE.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * sessionId가 존재하지 않을 때 (api.md 3.2, 3.4의 404 케이스).
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class ChatSessionNotFoundException extends ErrorResponseException {

    public ChatSessionNotFoundException(String detail) {
        super(HttpStatus.NOT_FOUND);
        setDetail(detail);
    }
}
