package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatType;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * GET /api/chat/sessions 응답(api.md 3.3)의 배열 원소.
 */
public record ChatSessionSummaryResponse(
        UUID sessionId,
        ChatType type,
        String title,
        OffsetDateTime createdAt
) {
}
