package com.example.phantommask.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer daysOfWeek; // 1=Mon, 7=Sun
    @Column(nullable = false)
    private LocalTime openTime;   // e.g., "09:00"
    @Column(nullable = false)
    private LocalTime closeTime;  // e.g., "18:00"

    private boolean overnight; // 是否跨午夜

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    @JsonIgnore
    private Pharmacies pharmacy;
}
