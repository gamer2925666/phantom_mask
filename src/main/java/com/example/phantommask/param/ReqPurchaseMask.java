package com.example.phantommask.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ReqPurchaseMask {
    @Schema(description = "藥局名稱", example = "HealthMart")
    private String pharmacyName;
    @Schema(description = "口罩名稱", example = "Cotton Kiss")
    private String maskName;
}
