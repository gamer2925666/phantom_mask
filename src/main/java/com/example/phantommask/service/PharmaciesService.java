package com.example.phantommask.service;

import com.example.phantommask.enums.Comparison;
import com.example.phantommask.enums.DayOfWeekEnum;
import com.example.phantommask.enums.MaskSortEnum;
import com.example.phantommask.param.*;
import com.example.phantommask.repository.entity.Mask;
import com.example.phantommask.repository.entity.Pharmacies;
import com.example.phantommask.repository.entity.User;
import org.springframework.data.domain.Sort;

import java.time.LocalTime;
import java.util.List;

public interface PharmaciesService {

    /**
     * 查詢營業中的藥局
     * @param time 時間
     * @param dayOfWeek 禮拜幾
     * @return 藥局列表
     */
    List<Pharmacies> getPharmacies(LocalTime time,DayOfWeekEnum dayOfWeek);

    /**
     * 查詢藥局口罩由名稱
     * @param name 藥局名稱
     * @param sortBy 排序欄位
     * @param order 排序方式
     * @return 口罩列表
     */
    List<Mask> getMasks(String name,
                        MaskSortEnum sortBy,
                        Sort.Direction order);

    /**
     * 查詢藥局與口罩由數量與價格
     *
     * @param maskCount  口罩數量
     * @param comparison 比較方式
     * @param minPrice   最低價格
     * @param maxPrice   最高價格
     * @return 藥局列表
     */
    List<ResFilterPharmacies> filterPharmacies(Integer maskCount,
                                               Comparison comparison,
                                               Double minPrice,
                                               Double maxPrice);

    /**
     * 取得購買口罩前 N 名的使用者
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @param topX 前 N 名
     * @return 使用者列表
     */
    List<ResPurchaseUser> getTopUsersPurchase(java.time.LocalDate startDate,
                                              java.time.LocalDate endDate,
                                              Integer topX);

    /**
     * 取得購買口罩的統計資料
     * @param startDate 開始日期
     * @param endDate 結束日期
     * @return 購買統計資料
     */
    ResPurchaseHistorySummary getPurchaseHistorySummary(java.time.LocalDate startDate,
                                                        java.time.LocalDate endDate);

    /**
     * 搜尋藥局與口罩
     *
     * @param keyword 關鍵字
     * @return 搜尋結果
     */
    List<ResSearchResult> searchPharmaciesAndMasks(String keyword);

    /**
     * 購買口罩
     *
     * @param reqPurchase 購買請求列表
     */
    void purchaseMask(ReqPurchase reqPurchase);

    /**
     * 取得購買紀錄
     *
     * @param userName 使用者名稱
     * @return 購買紀錄
     */
    User getUser(String userName);
}
