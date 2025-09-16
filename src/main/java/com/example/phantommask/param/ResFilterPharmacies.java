package com.example.phantommask.param;

import com.example.phantommask.repository.entity.Mask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ResFilterPharmacies {
    @Schema(description = "藥局名稱", example = "Health Element")
    private String pharmacyName;

    private List<Mask> masks = new ArrayList<>();
}
