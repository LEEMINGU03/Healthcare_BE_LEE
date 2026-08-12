package com.example.Healthcare_BE.workout.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * POST /api/workout-logs에서 routineId가 다른 유저의 루틴을 가리키는 경우의 400 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class InvalidWorkoutLogRequestException extends ErrorResponseException {

    public InvalidWorkoutLogRequestException(String detail) {
        super(HttpStatus.BAD_REQUEST);
        setDetail(detail);
    }
}
