package com.example.Healthcare_BE.mealplan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "meal_plan_meals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealPlanMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", nullable = false)
    private MealPlanDay day;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealSlot slot;

    @Column(nullable = false)
    private String menu;

    private Integer calories;

    @Column(name = "carbs_g")
    private BigDecimal carbsG;

    @Column(name = "protein_g")
    private BigDecimal proteinG;

    @Column(name = "fat_g")
    private BigDecimal fatG;

    public MealPlanMeal(MealSlot slot, String menu, Integer calories,
                         BigDecimal carbsG, BigDecimal proteinG, BigDecimal fatG) {
        this.slot = slot;
        this.menu = menu;
        this.calories = calories;
        this.carbsG = carbsG;
        this.proteinG = proteinG;
        this.fatG = fatG;
    }

    void assignDay(MealPlanDay day) {
        this.day = day;
    }
}
