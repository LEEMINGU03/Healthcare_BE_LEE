package com.example.Healthcare_BE.user.repository;

import com.example.Healthcare_BE.user.entity.AuthProvider;
import com.example.Healthcare_BE.user.entity.UserSocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSocialAccountRepository extends JpaRepository<UserSocialAccount, UUID> {

    Optional<UserSocialAccount> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);
}
