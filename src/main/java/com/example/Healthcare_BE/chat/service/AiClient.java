package com.example.Healthcare_BE.chat.service;

import com.example.Healthcare_BE.chat.dto.AiGenerateRequest;
import com.example.Healthcare_BE.chat.dto.AiGenerateResponse;

/**
 * AI 서버 호출 계약 (api.md 4장). base URL·인증 방식이 아직 미정이라(api.md 5장)
 * 실제 구현은 NotImplementedAiClient로 대체되어 있다 — 합의되는 대로 RestClient 기반
 * 구현으로 교체한다.
 */
public interface AiClient {

    AiGenerateResponse generate(AiGenerateRequest request);
}
