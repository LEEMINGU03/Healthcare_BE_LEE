package com.example.Healthcare_BE.chat.dto;

import java.util.UUID;

/**
 * POST /api/chat 응답(api.md 3.2).
 */
public record ChatResponse(
        UUID sessionId,
        String reply,
        ChatResultDto result
) {
}
