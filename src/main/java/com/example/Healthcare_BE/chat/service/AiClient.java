package com.example.Healthcare_BE.chat.service;

import com.example.Healthcare_BE.chat.dto.AiGenerateRequest;
import com.example.Healthcare_BE.chat.dto.AiGenerateResponse;

/**
 * AI 서버 호출 계약 (api.md 4장). 실제 구현은 RestClientAiClient.
 */
public interface AiClient {

    AiGenerateResponse generate(AiGenerateRequest request);
}
