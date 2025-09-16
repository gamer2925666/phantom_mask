package com.example.phantommask.utils;

import com.example.phantommask.param.MaskNamePojo;

public class MaskNameParser {


    public static MaskNamePojo parse(String maskName) {
        // 先把括號拆開
        String[] parts = maskName.split("\\(");

        String name = parts[0].trim();
        String color = parts[1].replace(")", "").trim();
        String pack = parts[2].replace(")", "").trim();
        // 用正則抓出數字
        int packSize = 0;
        String digits = pack.replaceAll("[^0-9]", "");
        if (!digits.isEmpty()) {
            packSize = Integer.parseInt(digits);
        }
        return new MaskNamePojo(name, color, packSize);
    }
}
