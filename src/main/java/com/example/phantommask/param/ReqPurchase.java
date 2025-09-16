package com.example.phantommask.param;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class ReqPurchase {

    @Schema(description = "使用者名稱", example = "Yvonne Guerrero")
    private String userName;

    @Schema(description = "購買口罩清單")
    private List<ReqPurchaseMask> purchaseMasks;
}
