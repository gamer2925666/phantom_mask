package com.example.phantommask.enums;

import lombok.Getter;

@Getter
public enum MaskSortEnum {
    PRICE("price"),
    NAME("name");

    private final String value;


    MaskSortEnum(String value) {
        this.value = value;
    }

    public static MaskSortEnum fromString(String day) {
        return switch (day.toLowerCase()) {
            case "price" -> PRICE;
            case "name" -> NAME;
            default -> PRICE;
        };
    }
}
