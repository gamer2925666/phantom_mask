package com.example.phantommask.param;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDto {

    private String pharmacyName;

    private String maskName;

    private Double transactionAmount;

    private LocalDateTime transactionDate;
}
