package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatType;

import java.util.List;
import java.util.UUID;

/**
 * GET /api/chat/sessions/{sessionId} 응답(api.md 3.4).
 */
public record ChatSessionDetailResponse(
        UUID sessionId,
        ChatType type,
        List<ChatMessageDto> messages
) {
}
