package com.example.phantommask.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "pharmacies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pharmacies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double cashBalance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pharmacy_id")
    private List<OpeningHour> openingHours;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pharmacy_id")
    private List<Mask> masks;

}
