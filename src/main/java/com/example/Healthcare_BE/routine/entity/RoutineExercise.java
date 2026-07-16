package com.example.Healthcare_BE.routine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.UUID;

@Entity
@Table(name = "routine_exercises")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sets;

    @Column(nullable = false)
    private String reps;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    public RoutineExercise(Integer orderNo, String name, String sets, String reps,
                            String description, String imageUrl) {
        this.orderNo = orderNo;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    void assignRoutine(Routine routine) {
        this.routine = routine;
    }
}
