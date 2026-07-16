package com.example.Healthcare_BE.inbody.entity;

import com.example.Healthcare_BE.user.entity.User;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inbody_records")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InbodyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "measured_at", nullable = false)
    private LocalDate measuredAt;

    @Column(name = "weight_kg", nullable = false)
    private BigDecimal weightKg;

    @Column(name = "bmr_kcal", nullable = false)
    private Integer bmrKcal;

    @Column(name = "skeletal_muscle_mass_kg", nullable = false)
    private BigDecimal skeletalMuscleMassKg;

    @Column(name = "body_fat_mass_kg", nullable = false)
    private BigDecimal bodyFatMassKg;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public InbodyRecord(User user, LocalDate measuredAt, BigDecimal weightKg, Integer bmrKcal,
                         BigDecimal skeletalMuscleMassKg, BigDecimal bodyFatMassKg) {
        this.user = user;
        this.measuredAt = measuredAt;
        this.weightKg = weightKg;
        this.bmrKcal = bmrKcal;
        this.skeletalMuscleMassKg = skeletalMuscleMassKg;
        this.bodyFatMassKg = bodyFatMassKg;
    }
}
