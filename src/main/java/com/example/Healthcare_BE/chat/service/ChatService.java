package com.example.Healthcare_BE.chat.service;

import com.example.Healthcare_BE.chat.dto.AiGenerateRequest;
import com.example.Healthcare_BE.chat.dto.AiGenerateResponse;
import com.example.Healthcare_BE.chat.dto.AiHistoryMessage;
import com.example.Healthcare_BE.chat.dto.ChatMessageDto;
import com.example.Healthcare_BE.chat.dto.ChatRequest;
import com.example.Healthcare_BE.chat.dto.ChatResponse;
import com.example.Healthcare_BE.chat.dto.ChatResultDto;
import com.example.Healthcare_BE.chat.dto.ChatSessionDetailResponse;
import com.example.Healthcare_BE.chat.dto.ChatSessionSummaryResponse;
import com.example.Healthcare_BE.chat.entity.ChatMessage;
import com.example.Healthcare_BE.chat.entity.ChatRole;
import com.example.Healthcare_BE.chat.entity.ChatSession;
import com.example.Healthcare_BE.chat.entity.ChatType;
import com.example.Healthcare_BE.chat.repository.ChatMessageRepository;
import com.example.Healthcare_BE.chat.repository.ChatSessionRepository;
import com.example.Healthcare_BE.inbody.dto.InbodyRecentResponse;
import com.example.Healthcare_BE.inbody.service.InbodyService;
import com.example.Healthcare_BE.mealplan.service.MealPlanService;
import com.example.Healthcare_BE.routine.service.RoutineService;
import com.example.Healthcare_BE.user.entity.User;
import com.example.Healthcare_BE.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * architecture.md 2장 "핵심 플로우"를 그대로 구현한다.
 * message가 비어있고 sessionId가 null이면 인사말 요청(api.md 3.2)으로 취급한다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private static final int TITLE_MAX_LENGTH = 20;
    private static final String NEW_SESSION_TITLE = "새 채팅";

    private final UserService userService;
    private final InbodyService inbodyService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RoutineService routineService;
    private final MealPlanService mealPlanService;
    private final AiClient aiClient;

    public ChatResponse chat(ChatRequest request) {
        boolean isGreeting = request.message() == null || request.message().isBlank();
        User currentUser = userService.getCurrentUser();

        ChatSession session;
        List<AiHistoryMessage> history;
        if (request.sessionId() == null) {
            session = chatSessionRepository.save(
                    new ChatSession(currentUser, request.type(), buildTitle(request.message(), isGreeting)));
            history = List.of();
        } else {
            session = chatSessionRepository.findById(request.sessionId())
                    .orElseThrow(() -> new ChatSessionNotFoundException("세션을 찾을 수 없습니다: " + request.sessionId()));
            if (!session.getUser().getId().equals(currentUser.getId()) || session.getType() != request.type()) {
                throw new InvalidChatRequestException("sessionId가 다른 유저/타입의 세션을 가리킵니다.");
            }
            if (isGreeting) {
                throw new InvalidChatRequestException("기존 세션에는 message가 필요합니다.");
            }
            history = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()).stream()
                    .map(message -> new AiHistoryMessage(message.getRole(), message.getContent()))
                    .toList();
        }

        InbodyRecentResponse inbody = inbodyService.findRecent(currentUser.getId()).orElse(null);

        AiGenerateRequest aiRequest = new AiGenerateRequest(
                request.type(),
                isGreeting ? null : request.message(),
                request.settings(),
                userService.getProfile(),
                inbody,
                history);
        AiGenerateResponse aiResponse = aiClient.generate(aiRequest);

        if (!isGreeting) {
            chatMessageRepository.save(new ChatMessage(session, ChatRole.USER, request.message()));
        }
        ChatMessage assistantMessage = chatMessageRepository.save(
                new ChatMessage(session, ChatRole.ASSISTANT, aiResponse.reply()));

        persistResult(assistantMessage, aiResponse.result());

        return new ChatResponse(session.getId(), aiResponse.reply(), aiResponse.result());
    }

    public List<ChatSessionSummaryResponse> getSessions(ChatType type) {
        User currentUser = userService.getCurrentUser();
        return chatSessionRepository.findByUserIdAndTypeOrderByCreatedAtDesc(currentUser.getId(), type).stream()
                .map(session -> new ChatSessionSummaryResponse(
                        session.getId(), session.getType(), session.getTitle(), session.getCreatedAt()))
                .toList();
    }

    public ChatSessionDetailResponse getSessionDetail(UUID sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("세션을 찾을 수 없습니다: " + sessionId));
        List<ChatMessageDto> messages = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(this::toMessageDto)
                .toList();
        return new ChatSessionDetailResponse(session.getId(), session.getType(), messages);
    }

    private void persistResult(ChatMessage assistantMessage, ChatResultDto result) {
        if (result == null) {
            return;
        }
        if (result.routine() != null) {
            routineService.saveRoutine(assistantMessage, result.routine());
        }
        if (result.mealPlan() != null) {
            mealPlanService.saveMealPlan(assistantMessage, result.mealPlan());
        }
    }

    private ChatMessageDto toMessageDto(ChatMessage message) {
        ChatResultDto result = message.getRole() == ChatRole.ASSISTANT ? findResult(message.getId()) : null;
        return new ChatMessageDto(message.getRole(), message.getContent(), result);
    }

    private ChatResultDto findResult(UUID chatMessageId) {
        return routineService.findByChatMessage(chatMessageId)
                .map(routine -> new ChatResultDto(routine, null))
                .or(() -> mealPlanService.findByChatMessage(chatMessageId)
                        .map(mealPlan -> new ChatResultDto(null, mealPlan)))
                .orElse(null);
    }

    private String buildTitle(String message, boolean isGreeting) {
        if (isGreeting) {
            return NEW_SESSION_TITLE;
        }
        return message.length() <= TITLE_MAX_LENGTH ? message : message.substring(0, TITLE_MAX_LENGTH);
    }
}
