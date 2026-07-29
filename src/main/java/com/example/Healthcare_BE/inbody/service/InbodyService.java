package com.example.Healthcare_BE.inbody.service;

import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.inbody.entity.InbodyRecord;
import com.example.Healthcare_BE.inbody.repository.InbodyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InbodyService {

    private final InbodyRecordRepository inbodyRecordRepository;

    /** GET /api/inbody/recent 용 — 기록이 없으면 404 (api.md 3.1). */
    public InbodyRecentResponse getRecent(UUID userId) {
        return findRecent(userId)
                .orElseThrow(() -> new InbodyRecordNotFoundException("인바디 기록이 없습니다."));
    }

    /** AI 요청 조립용 — 기록이 없으면 null을 그대로 보낸다 (api.md 4.1). */
    public Optional<InbodyRecentResponse> findRecent(UUID userId) {
        return inbodyRecordRepository.findFirstByUserIdOrderByMeasuredAtDesc(userId).map(this::toDto);
    }

    private InbodyRecentResponse toDto(InbodyRecord record) {
        return new InbodyRecentResponse(
                record.getMeasuredAt(),
                record.getWeightKg(),
                record.getSkeletalMuscleMassKg(),
                record.getBodyFatMassKg(),
                record.getBodyFatPct(),
                record.getBmrKcal());
    }
}
