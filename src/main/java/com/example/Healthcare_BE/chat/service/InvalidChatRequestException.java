package com.example.Healthcare_BE.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * message 누락(기존 세션인데 비어있음), sessionId가 다른 유저/타입의 세션을 가리키는 경우
 * (api.md 3.2의 400 케이스). ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class InvalidChatRequestException extends ErrorResponseException {

    public InvalidChatRequestException(String detail) {
        super(HttpStatus.BAD_REQUEST);
        setDetail(detail);
    }
}
