package com.example.phantommask.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;

/*
    回傳給前端的搜尋結果
*/
@Getter
@Setter
@ToString
@Builder
public class ResSearchResult {
    @Schema(description = "藥局或口罩名稱", example = "HealthMart")
    private String name;
    @Schema(description = "類型", example = "pharmacy", allowableValues = {"pharmacy", "mask"})
    private String type;
    @Schema(description = "相關性分數", example = "10")
    private int relevance;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ResSearchResult that = (ResSearchResult) object;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
