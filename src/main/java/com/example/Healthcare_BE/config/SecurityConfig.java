package com.example.Healthcare_BE.config;

import java.util.List;

import com.example.Healthcare_BE.auth.service.CustomOAuth2UserService;
import com.example.Healthcare_BE.auth.service.JwtAuthenticationFilter;
import com.example.Healthcare_BE.auth.service.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * MVP는 로그인이 없다 — 전 요청 permitAll (architecture.md 6장).
 * 세션/쿠키 기반 인증이 없는 stateless JSON API라 CSRF 보호 대상이 아니어서 비활성화한다.
 * 프론트가 별도 오리진(Vercel)이라 CORS 허용이 필요하다 (architecture.md 6장).
 *
 * 구글 소셜 로그인 2단계: CustomOAuth2UserService로 로그인 성공 시 users/user_social_accounts를 연동한다.
 * 3단계: 세션 대신 OAuth2AuthenticationSuccessHandler가 Access/Refresh Token을 발급해 프론트로 리다이렉트한다.
 * 4단계: JwtAuthenticationFilter가 매 요청의 Authorization 헤더를 검증해 SecurityContext에 로그인 유저를 세팅한다
 * (UserService.getCurrentUser() 참고). 아직 permitAll이라 토큰 없이도 API 호출은 가능하다.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                     CustomOAuth2UserService customOAuth2UserService,
                                                     OAuth2AuthenticationSuccessHandler successHandler,
                                                     JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://healthchatbot-front-end-u557.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
