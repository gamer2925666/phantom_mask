package com.example.phantommask.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_histories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "藥局名稱")
    private String pharmacyName;

    @Column(nullable = false)
    @Schema(description = "口罩名稱")
    private String maskName;

    @Column(nullable = false)
    @JsonIgnore
    private String maskColor;

    @Column(nullable = false)
    @JsonIgnore
    private Integer maskPackSize;

    @Column(nullable = false)
    @Schema(description = "交易金額")
    private Double transactionAmount;

    @Column(nullable = false)
    @Schema(description = "交易時間")
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
