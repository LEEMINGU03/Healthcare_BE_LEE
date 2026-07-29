package com.example.Healthcare_BE.auth.service;

import com.example.Healthcare_BE.user.entity.AuthProvider;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.entity.UserSocialAccount;
import com.example.Healthcare_BE.user.repository.UserRepository;
import com.example.Healthcare_BE.user.repository.UserSocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구글 로그인 성공 시 호출된다. 구글 sub로 기존 유저를 찾고, 없으면 이름만 채운 최소 유저를 새로 만든다.
 * 신체 정보(성별·키 등)는 이 시점엔 없다 — 최초 로그인한 사람은 별도 입력 페이지로 넘어가 채운다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserSocialAccountRepository userSocialAccountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerUserId = oAuth2User.getAttribute("sub");
        String name = oAuth2User.getAttribute("name");

        User user = userSocialAccountRepository
                .findByProviderAndProviderUserId(AuthProvider.GOOGLE, providerUserId)
                .map(UserSocialAccount::getUser)
                .orElseGet(() -> createUser(name, providerUserId));

        return new CustomOAuth2User(oAuth2User, user.getId());
    }

    private User createUser(String name, String providerUserId) {
        User user = userRepository.save(new User(name));
        userSocialAccountRepository.save(new UserSocialAccount(user, AuthProvider.GOOGLE, providerUserId));
        return user;
    }
}
