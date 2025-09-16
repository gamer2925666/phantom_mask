package com.example.phantommask.utils;

import com.example.phantommask.enums.DayOfWeekEnum;
import com.example.phantommask.repository.entity.OpeningHour;

import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpeningHoursParser {
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{2}:\\d{2})\\s*-\\s*(\\d{2}:\\d{2})");

    public static List<OpeningHour> parse(String input) {
        List<OpeningHour> result = new ArrayList<>();

        // 1. 分段（使用 / 分隔）
        String[] segments = input.split("\\s*/\\s*");

        for (String segment : segments) {
            // 2. 找時間區間
            Matcher matcher = TIME_PATTERN.matcher(segment);
            if (!matcher.find()) continue;

            String openStr = matcher.group(1);
            String closeStr = matcher.group(2);

            LocalTime openTime = LocalTime.parse(openStr);
            LocalTime closeTime = LocalTime.parse(closeStr);
            boolean overnight = closeTime.isBefore(openTime);

            // 3. 拆出天數部分（時間區間前的字串）
            String daysPart = segment.substring(0, matcher.start()).trim();

            // 4. 處理範圍（-）或逗號分隔
            Set<Integer> days = new LinkedHashSet<>();

            if (daysPart.contains("-")) {
                String[] range = daysPart.split("-");
                if (range.length == 2) {
                    Integer start = DayOfWeekEnum.fromString(range[0].trim()).getValue();
                    Integer end = DayOfWeekEnum.fromString(range[1].trim()).getValue();
                    if (start != null && end != null) {
                        int i = start;
                        while (true) {
                            days.add(i);
                            if (i == end) break;
                            i = i % 7 + 1; // 循環週日->週一
                        }
                    }
                }
            } else if (daysPart.contains(",")) {
                String[] dayNames = daysPart.split(",");
                for (String name : dayNames) {
                    Integer day = DayOfWeekEnum.fromString(name.trim()).getValue();
                    if (day != null) days.add(day);
                }
            } else {
                Integer day = DayOfWeekEnum.fromString(daysPart.trim()).getValue();
                if (day != null) days.add(day);
            }

            // 5. 產生 OpeningHour 物件
            for (Integer day : days) {
                result.add(OpeningHour.builder()
                        .daysOfWeek(day)
                        .openTime(openTime)
                        .closeTime(closeTime)
                        .overnight(overnight)
                        .build());
            }
        }

        return result;
    }
}
