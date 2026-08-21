package com.example.Healthcare_BE.auth.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * 구글이 준 원본 속성(delegate)에 우리 DB의 유저 id를 얹어서 들고 다닌다.
 * 로그인 성공 핸들러(3단계)가 이 id로 JWT를 발급한다.
 */
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User delegate;
    private final UUID userId;

    public CustomOAuth2User(OAuth2User delegate, UUID userId) {
        this.delegate = delegate;
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
