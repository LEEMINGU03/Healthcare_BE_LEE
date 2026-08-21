package com.example.Healthcare_BE.chat.dto;

import com.example.Healthcare_BE.chat.entity.ChatType;
import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.user.dto.UserProfileDto;

import java.util.List;

/**
 * POST {ai.base-url}/generate 요청(api.md 4.1). 인사말 요청일 때는 message=null,
 * history=빈 리스트로 보낸다(api.md 3.2/4.1).
 */
public record AiGenerateRequest(
        ChatType type,
        String message,
        ChatSettingsRequest settings,
        UserProfileDto profile,
        InbodyRecentResponse inbody,
        List<AiHistoryMessage> history
) {
}
