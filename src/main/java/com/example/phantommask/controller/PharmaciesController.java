package com.example.phantommask.controller;

import com.example.phantommask.enums.Comparison;
import com.example.phantommask.enums.DayOfWeekEnum;
import com.example.phantommask.enums.MaskSortEnum;
import com.example.phantommask.param.*;
import com.example.phantommask.repository.entity.User;
import com.example.phantommask.repository.repository.MaskRepository;
import com.example.phantommask.repository.entity.Mask;
import com.example.phantommask.repository.entity.Pharmacies;
import com.example.phantommask.service.PharmaciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
public class PharmaciesController {

    private PharmaciesService pharmaciesService;

    public PharmaciesController(PharmaciesService pharmaciesService) {
        this.pharmaciesService = pharmaciesService;
    }

    @Operation(summary = "查詢營業中的藥局", description = "根據星期與時間查詢營業中的藥局")
    @GetMapping("/pharmacies")
    public ResponseEntity<ApiResponse<List<Pharmacies>>> getPharmacies(
            @RequestParam
            @Parameter(name = "time",description = "查詢時間", example = "09:00",schema = @Schema(type = "string", format = "time"))
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime time,
            @RequestParam(required = false)
            @Parameter(name = "dayOfWeek",description = "禮拜幾", schema = @Schema(implementation = DayOfWeekEnum.class))
            DayOfWeekEnum dayOfWeek
    ) {
        log.info("getPharmacies time={}, dayOfWeek={}", time, dayOfWeek);
        return ResponseEntity.ok(
                ApiResponse.<List<Pharmacies>>builder()
                        .data(pharmaciesService.getPharmacies(time, dayOfWeek))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary = "查詢藥局口罩", description = "列出指定藥局所販售的所有口罩，並可依照口罩名稱或價格進行排序。")
    @GetMapping("/pharmacies/masks")
    public ResponseEntity<ApiResponse<List<Mask>>> getMasks(
            @RequestParam
            @Parameter(name = "name",description = "藥局名稱", example = "HealthMart")
            String name,
            @RequestParam(required = false, defaultValue = "name")
            @Parameter(name="sortBy",description = "排序欄位", example = "price",schema = @Schema(implementation = MaskSortEnum.class))
            MaskSortEnum sortBy,
            @RequestParam(required = false, defaultValue = "DESC")
            @Parameter(name="order",description = "排序方式", schema = @Schema(implementation = Sort.Direction.class))
            Sort.Direction order
    ){
        log.info("getMasks name={}, sortBy={}, order={}", name, sortBy, order);
        return ResponseEntity.ok(
                ApiResponse.<List<Mask>>builder()
                        .data(pharmaciesService.getMasks(name, sortBy, order))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary = "查詢藥局與口罩由數量與價格",description = "口罩商品數量多於或少於 x，且價格在指定範圍內")
    @GetMapping("/pharmacies/filter")
    public ResponseEntity<ApiResponse<List<ResFilterPharmacies>>> filterPharmacies(
            @RequestParam
            @Parameter(name = "maskCount",description = "口罩商品數量", example = "5")
            Integer maskCount,
            @RequestParam
            @Parameter(name = "comparison",description = "比較方式", example = "MORE_THAN", schema = @Schema(implementation = Comparison.class))
            Comparison comparison,
            @RequestParam
            @Parameter(name = "minPrice",description = "最低價格", example = "10.0")
            Double minPrice,
            @RequestParam
            @Parameter(name = "maxPrice",description = "最高價格", example = "50.0")
            Double maxPrice
    ){
        log.info("filterPharmacies maskCount={}, comparison={}, minPrice={}, maxPrice={}", maskCount, comparison, minPrice, maxPrice);
        return ResponseEntity.ok(
                ApiResponse.<List<ResFilterPharmacies>>builder()
                        .data(pharmaciesService.filterPharmacies(maskCount, comparison, minPrice, maxPrice))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary = "消費榜",description = "在指定日期範圍內，依據口罩交易總金額，取得前 x 名的使用者。")
    @GetMapping("/users/top")
    public ResponseEntity<ApiResponse<List<ResPurchaseUser>>> getTopUsersPurchase(
            @RequestParam
            @Parameter(name = "startDate",description = "開始日期", example = "2023-01-01", schema = @Schema(type = "string", format = "date"))
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            java.time.LocalDate startDate,
            @RequestParam
            @Parameter(name = "endDate",description = "結束日期", example = "2023-12-31", schema = @Schema(type = "string", format = "date"))
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            java.time.LocalDate endDate,
            @RequestParam
            @Parameter(name = "topX",description = "前幾名", example = "10")
            Integer topX
    ){
        log.info("getTopUsers startDate={}, endDate={}, topX={}", startDate, endDate, topX);
        return ResponseEntity.ok(
                ApiResponse.<List<ResPurchaseUser>>builder()
                        .data(pharmaciesService.getTopUsersPurchase(startDate, endDate, topX))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary = "計算口罩交易總數量總金額",description = "計算在指定日期範圍內，交易口罩的總數量與總交易金額")
    @GetMapping("/purchaseHistory/summary")
    public ResponseEntity<ApiResponse<ResPurchaseHistorySummary>> getPurchaseHistorySummary(
            @RequestParam
            @Parameter(name = "startDate",description = "開始日期", example = "2023-01-01", schema = @Schema(type = "string", format = "date"))
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            java.time.LocalDate startDate,
            @RequestParam
            @Parameter(name = "endDate",description = "結束日期", example = "2023-12-31", schema = @Schema(type = "string", format = "date"))
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            java.time.LocalDate endDate
    ){
        log.info("getPurchaseHistorySummary startDate={}, endDate={}", startDate, endDate);
        return ResponseEntity.ok(
                ApiResponse.<ResPurchaseHistorySummary>builder()
                        .data(pharmaciesService.getPurchaseHistorySummary(startDate, endDate))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary ="搜尋藥局或口罩",description = "依照名稱搜尋藥局或口罩，並根據與搜尋字詞的相關性對結果排序。")
    @GetMapping("/pharmacies/search")
    public ResponseEntity<ApiResponse<List<ResSearchResult>>> searchPharmaciesAndMasks(
            @RequestParam
            @Parameter(name = "keyword",description = "搜尋字詞", example = "HealthMart")
            String keyword
    ){
        log.info("searchPharmaciesAndMasks keyword={}", keyword);
        return ResponseEntity.ok(
                ApiResponse.<List<ResSearchResult>>builder()
                        .data(pharmaciesService.searchPharmaciesAndMasks(keyword))
                        .code("200")
                        .build()
        );
    }

    @Operation(summary = "口罩購買")
    @PostMapping("/masks/purchase")
    public ResponseEntity<ApiResponse<Void>> purchaseMask(
            @RequestBody
            @Parameter(description = "購買口罩請求資料", required = true, schema = @Schema(implementation = ReqPurchase.class))
            ReqPurchase reqPurchases
    ){
        log.info("purchaseMask reqPurchases={}", reqPurchases);
        pharmaciesService.purchaseMask(reqPurchases);
        return null;
    }
}
