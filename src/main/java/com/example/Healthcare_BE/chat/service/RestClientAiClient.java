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
 * api.md 4장 POST {ai.base-url}/generate 호출. 타임아웃은 4.4에서 정한 연결 3초/읽기 30초 —
 * NUTRITION 응답이 실측 17초 넘게 걸리는 걸 확인하고 정한 값이라 30초 밑으로 줄이면 안 된다.
 */
@Component
class RestClientAiClient implements AiClient {

    private final RestClient restClient;

    RestClientAiClient(@Value("${ai.base-url}") String baseUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

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
