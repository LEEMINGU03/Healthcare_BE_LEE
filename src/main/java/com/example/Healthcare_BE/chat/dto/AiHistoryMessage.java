package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatRole;

/**
 * api.md 4.1 AI 요청의 history[] 원소.
 */
public record AiHistoryMessage(
        ChatRole role,
        String content
) {
}
