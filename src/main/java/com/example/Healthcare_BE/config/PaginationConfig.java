package com.example.Healthcare_BE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * GET /api/chat/sessions의 page/size 쿼리 파라미터가 Pageable에 바인딩되지 않던 버그 수정.
 * Spring Boot의 Pageable 자동설정(DataWebAutoConfiguration)은 다른 PageableHandlerMethodArgumentResolver
 * 빈이 먼저 등록되면 조용히 스킵되는 조건(@ConditionalOnMissingBean)을 갖고 있어, 암묵적 자동설정에
 * 기대지 않고 여기서 명시적으로 켠다.
 * pageSerializationMode=DIRECT로 고정 — VIA_DTO로 바뀌면 api.md 3.3에 문서화된 Page 그대로의
 * 응답 형식(content/totalElements/totalPages/number/size)이 깨진다.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.DIRECT)
public class PaginationConfig {
}
