package com.example.Healthcare_BE.chat.controller;

import com.example.Healthcare_BE.chat.dto.ChatRequest;
import com.example.Healthcare_BE.chat.dto.ChatResponse;
import com.example.Healthcare_BE.chat.dto.ChatSessionDetailResponse;
import com.example.Healthcare_BE.chat.dto.ChatSessionSummaryResponse;
import com.example.Healthcare_BE.chat.entity.ChatType;
import com.example.Healthcare_BE.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * api.md 3.2~3.4.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    @GetMapping("/sessions")
    public List<ChatSessionSummaryResponse> getSessions(@RequestParam ChatType type) {
        return chatService.getSessions(type);
    }

    @GetMapping("/sessions/{sessionId}")
    public ChatSessionDetailResponse getSessionDetail(@PathVariable UUID sessionId) {
        return chatService.getSessionDetail(sessionId);
    }
}
