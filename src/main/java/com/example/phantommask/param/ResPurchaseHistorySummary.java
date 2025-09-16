package com.example.phantommask.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/*
    回傳給前端的購買統計資料
*/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResPurchaseHistorySummary {
    @Schema(description = "總購買金額", example = "1500.75")
    private Double totalAmountSpent = 0.0;

    @Schema(description = "總購買口罩數量", example = "30")
    private Integer totalMasksPurchased = 0;
}
