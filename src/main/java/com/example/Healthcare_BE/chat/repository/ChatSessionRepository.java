package com.example.Healthcare_BE.chat.repository;

import com.example.Healthcare_BE.chat.entity.ChatSession;
import com.example.Healthcare_BE.chat.entity.ChatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    Page<ChatSession> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, ChatType type, Pageable pageable);
}
