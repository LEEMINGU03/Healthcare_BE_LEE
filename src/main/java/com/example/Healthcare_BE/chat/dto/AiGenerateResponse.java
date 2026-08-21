package com.example.Healthcare_BE.chat.dto;

/**
 * POST {ai.base-url}/generate 응답(api.md 4.1). result는 ChatResponse.result로 그대로 전달된다.
 */
public record AiGenerateResponse(
        String reply,
        ChatResultDto result
) {
}
