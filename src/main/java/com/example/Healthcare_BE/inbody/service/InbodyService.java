package com.example.Healthcare_BE.inbody.service;

import com.example.Healthcare_BE.inbody.dto.InbodyCreateRequest;
import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.inbody.entity.InbodyRecord;
import com.example.Healthcare_BE.inbody.repository.InbodyRecordRepository;
import com.example.Healthcare_BE.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    /**
     * POST /api/inbody — 회원가입2 + 이후 재측정 (signup_profile_api_spec.md 2-4).
     * 같은 유저·같은 measuredAt이 이미 있으면 409(미결사항 확정).
     */
    @Transactional
    public InbodyRecentResponse create(User user, InbodyCreateRequest request) {
        if (inbodyRecordRepository.existsByUserIdAndMeasuredAt(user.getId(), request.measuredAt())) {
            throw new DuplicateInbodyRecordException("이미 해당 날짜의 인바디 기록이 있습니다: " + request.measuredAt());
        }
        InbodyRecord record = new InbodyRecord(user, request.measuredAt(), request.weightKg(), request.bmrKcal(),
                request.skeletalMuscleMassKg(), request.bodyFatMassKg(), request.bodyFatPct());
        inbodyRecordRepository.save(record);
        return toDto(record);
    }

    /** GET /api/inbody 용 — 측정 이력 전체, 최신순 (api.md 3.1b). 기록이 없으면 빈 배열. */
    public List<InbodyRecentResponse> getHistory(UUID userId) {
        return inbodyRecordRepository.findByUserIdOrderByMeasuredAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
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
