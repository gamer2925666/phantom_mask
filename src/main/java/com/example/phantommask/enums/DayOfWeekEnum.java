package com.example.phantommask.enums;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
public enum DayOfWeekEnum {
    MON(1), TUE(2), WED(3), THU(4), FRI(5), SAT(6), SUN(7);

    private final int value;

    DayOfWeekEnum(int value) {
        this.value = value;
    }

    // 透過字串取得 enum
    public static DayOfWeekEnum fromString(String day) {
        return switch (day.toLowerCase()) {
            case "mon" -> MON;
            case "tue" -> TUE;
            case "wed" -> WED;
            case "thu" -> THU;
            case "thur" -> THU; // 支援 Thur
            case "fri" -> FRI;
            case "sat" -> SAT;
            case "sun" -> SUN;
            default -> throw new IllegalArgumentException("Unknown day: " + day);
        };
    }
    public static DayOfWeekEnum today() {
        DayOfWeek javaDay = LocalDate.now().getDayOfWeek();
        return switch (javaDay) {
            case MONDAY -> MON;
            case TUESDAY -> TUE;
            case WEDNESDAY -> WED;
            case THURSDAY -> THU;
            case FRIDAY -> FRI;
            case SATURDAY -> SAT;
            case SUNDAY -> SUN;
        };
    }
}
