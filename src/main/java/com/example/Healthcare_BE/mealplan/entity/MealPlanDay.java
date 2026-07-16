package com.example.Healthcare_BE.mealplan.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meal_plan_days")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealPlanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private MealPlanDayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanMeal> meals = new ArrayList<>();

    public MealPlanDay(MealPlanDayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    void assignMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    public void addMeal(MealPlanMeal meal) {
        meals.add(meal);
        meal.assignDay(this);
    }
}
