package com.example.phantommask.param;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;

    private Double cashBalance;

    private List<PurchaseHistoryDto> purchaseHistories;
}
