package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatRole;

/**
 * GET /api/chat/sessions/{sessionId} 응답(api.md 3.4)의 messages[] 원소.
 */
public record ChatMessageDto(
        ChatRole role,
        String content,
        ChatResultDto result
) {
}
