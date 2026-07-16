package com.example.Healthcare_BE.mealplan.entity;

import com.example.Healthcare_BE.chat.entity.ChatMessage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meal_plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false, unique = true)
    private ChatMessage chatMessage;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanDay> days = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public MealPlan(ChatMessage chatMessage, String title) {
        this.chatMessage = chatMessage;
        this.title = title;
    }

    public void addDay(MealPlanDay day) {
        days.add(day);
        day.assignMealPlan(this);
    }
}
