package com.example.Healthcare_BE.chat.service;

import com.example.Healthcare_BE.chat.dto.AiGenerateRequest;
import com.example.Healthcare_BE.chat.dto.AiGenerateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * api.md 4장 POST {ai.base-url}/generate 호출. 연결 타임아웃 3초는 유지.
 * 읽기 타임아웃은 원래 30초였으나(NUTRITION 단순 프롬프트 기준 실측 17초), NUTRITION에
 * search_food_nutrition 실제 조회(식약처 MFDS/FatSecret)가 붙으면서 7일 식단표처럼 메뉴가
 * 많은 요청은 65초 안팎(도구 호출 1회로 정리한 뒤 실측)까지 걸리는 걸 확인해 여유를 두고
 * 90초로 늘렸다 — 임시 조치. 이 시간의 대부분은 모델이 도구를 여러 번 재호출해서 낭비하는
 * 게 아니라, (1) 어떤 메뉴를 조회할지 정하는 첫 호출, (2) 식약처/FatSecret 실제 조회,
 * (3) 21끼 분량의 구조화 JSON을 실제로 생성하는 마지막 호출 — 이 세 단계 자체가 원래 오래
 * 걸린다. 더 줄이려면 AI 서버 쪽에서 응답을 스트리밍하거나 식단표 생성을 비동기로 바꿔야 한다.
 */
@Component
class RestClientAiClient implements AiClient {

    private final RestClient restClient;

    RestClientAiClient(@Value("${ai.base-url}") String baseUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(90));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public AiGenerateResponse generate(AiGenerateRequest request) {
        try {
            return restClient.post()
                    .uri("/generate")
                    .body(request)
                    .retrieve()
                    .body(AiGenerateResponse.class);
        } catch (RestClientException e) {
            throw new AiServerException("AI 서버 호출에 실패했습니다.", e);
        }
    }
}
