package com.example.Healthcare_BE.inbody.repository;

import com.example.Healthcare_BE.inbody.entity.InbodyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InbodyRecordRepository extends JpaRepository<InbodyRecord, UUID> {

    Optional<InbodyRecord> findFirstByUserIdOrderByMeasuredAtDesc(UUID userId);

    List<InbodyRecord> findByUserIdOrderByMeasuredAtDesc(UUID userId);

    boolean existsByUserIdAndMeasuredAt(UUID userId, LocalDate measuredAt);
}
