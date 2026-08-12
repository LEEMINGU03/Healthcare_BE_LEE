package com.example.Healthcare_BE.workout.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

/**
 * POST /api/workout-logs에서 routineId가 존재하지 않는 루틴을 가리키는 경우의 404 케이스.
 * ErrorResponseException 상속 이유는 InbodyRecordNotFoundException 주석 참고.
 */
public class RoutineNotFoundException extends ErrorResponseException {

    public RoutineNotFoundException(String detail) {
        super(HttpStatus.NOT_FOUND);
        setDetail(detail);
    }
}
