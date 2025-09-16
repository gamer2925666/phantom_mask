package com.example.phantommask.service.impl;

import com.example.phantommask.enums.Comparison;
import com.example.phantommask.enums.DayOfWeekEnum;
import com.example.phantommask.enums.MaskSortEnum;
import com.example.phantommask.param.*;
import com.example.phantommask.repository.entity.*;
import com.example.phantommask.repository.repository.*;
import com.example.phantommask.service.PharmaciesService;
import com.example.phantommask.utils.MaskNameParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class PharmaciesServiceImpl implements PharmaciesService {

    private PharmaciesRepository pharmaciesRepository;

    private MaskRepository maskRepository;

    private PurchaseHistoryRepository purchaseHistoryRepository;

    private UserRepository userRepository;

    private OpeningHourRepository openingHourRepository;

    public PharmaciesServiceImpl(PharmaciesRepository pharmaciesRepository,
                                MaskRepository maskRepository,
                                PurchaseHistoryRepository purchaseHistoryRepository,
                                UserRepository userRepository,
                                OpeningHourRepository openingHourRepository) {
        this.pharmaciesRepository = pharmaciesRepository;
        this.maskRepository = maskRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.userRepository = userRepository;
        this.openingHourRepository = openingHourRepository;
    }
    /**
     * 查詢營業中的藥局
     *
     * @param time      時間
     * @param dayOfWeek 禮拜幾
     * @return 藥局列表
     */
    @Override
    public List<Pharmacies> getPharmacies(LocalTime time, DayOfWeekEnum dayOfWeek) {
        if (dayOfWeek==null){
            dayOfWeek = DayOfWeekEnum.today();
        }
        // 處理今日與昨日，昨日作為查詢跨日使用
        int today = dayOfWeek.getValue();
        int yesterday = today == 1 ? 7 : today - 1;
        return openingHourRepository.findAllByDayAndTimeWithOvernight(today, yesterday, time).stream().map(OpeningHour::getPharmacy).toList();
    }

    /**
     * 查詢藥局口罩由名稱
     *
     * @param name   藥局名稱
     * @param sortBy 排序欄位
     * @param order  排序方式
     * @return 口罩列表
     */
    @Override
    public List<Mask> getMasks(String name, MaskSortEnum sortBy, Sort.Direction order) {
        // 根據藥局名稱查詢藥局
        Pharmacies pharmacies = pharmaciesRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        // 根據排序欄位與排序方式定義比較器
        Comparator<Mask> comparator = sortBy == MaskSortEnum.NAME ?
                Comparator.comparing(Mask::getName) :
                Comparator.comparing(Mask::getPrice);
        // 如果是降序，則反轉比較器
        if (order == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        // 返回排序後的口罩列表
        return pharmacies.getMasks().stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * 查詢藥局與口罩由數量與價格
     *
     * @param maskCount  口罩數量
     * @param comparison 比較方式
     * @param minPrice   最低價格
     * @param maxPrice   最高價格
     * @return 藥局列表
     */
    @Override
    public List<ResFilterPharmacies> filterPharmacies(Integer maskCount, Comparison comparison, Double minPrice, Double maxPrice) {
        List<Mask> maskList = null;
        // 根據比較方式查詢口罩
        if (comparison.equals(Comparison.LESS_THAN)){
            maskList = maskRepository.findByPackSizeLessThanAndPriceBetween(maskCount, minPrice, maxPrice);
        } else {
            maskList = maskRepository.findByPackSizeGreaterThanAndPriceBetween(maskCount, minPrice, maxPrice);
        }
        // 將口罩按藥局分組
        Map<String,ResFilterPharmacies> resultMap = new HashMap<>();
        for (Mask mask : maskList) {
            String pharmacyName = mask.getPharmacy().getName();
            ResFilterPharmacies resFilterPharmacies = resultMap.getOrDefault(pharmacyName, new ResFilterPharmacies());
            resFilterPharmacies.setPharmacyName(pharmacyName);
            resFilterPharmacies.getMasks().add(mask);
            resultMap.put(pharmacyName, resFilterPharmacies);
        }

        return resultMap.values().stream().toList();
    }

    /**
     * 取得購買口罩前 N 名的使用者
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @param topX      前 N 名
     * @return 使用者列表
     */
    @Override
    public List<ResPurchaseUser> getTopUsersPurchase(LocalDate startDate, LocalDate endDate, Integer topX) {
        LocalDateTime startDateTime = startDate.atTime(0, 0, 0);
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return purchaseHistoryRepository.findTopUsersByTotalAmount(startDateTime, endDateTime, PageRequest.of(0, topX));
    }

    /**
     * 取得購買口罩的統計資料
     *
     * @param startDate 開始日期
     * @param endDate   結束日期
     * @return 購買統計資料
     */
    @Override
    public ResPurchaseHistorySummary getPurchaseHistorySummary(LocalDate startDate, LocalDate endDate) {
        // 查詢指定日期範圍內的購買記錄
        List<PurchaseHistory> purchaseHistories = purchaseHistoryRepository.findByTransactionDateBetween(
                startDate.atTime(0, 0, 0),
                endDate.atTime(23, 59, 59)
        );
        // 計算總交易金額
        ResPurchaseHistorySummary resPurchaseHistorySummary = new ResPurchaseHistorySummary();
        resPurchaseHistorySummary.setTotalAmountSpent(
                purchaseHistories.stream()
                        .mapToDouble(PurchaseHistory::getTransactionAmount)
                        .sum()
        );
        // 計算總購買口罩數量
        resPurchaseHistorySummary.setTotalMasksPurchased(
                purchaseHistories.stream()
                        .mapToInt(PurchaseHistory::getMaskPackSize)
                        .sum()
        );

        return resPurchaseHistorySummary;
    }

    /**
     * 搜尋藥局與口罩
     *
     * @param keyword 關鍵字
     * @return 搜尋結果
     */
    @Override
    public List<ResSearchResult> searchPharmaciesAndMasks(String keyword) {
        //第一階段使用like查詢，中了的話+1分
        List<Pharmacies> pharmaciesList = pharmaciesRepository.findByNameContainingIgnoreCase(keyword);
        List<Mask> maskList = maskRepository.findByNameContainingIgnoreCase(keyword);
        Set<ResSearchResult> resultSet = new HashSet<>();
        pharmaciesList.forEach(pharmacies -> resultSet.add(ResSearchResult.builder()
                .type("Pharmacy")
                .name(pharmacies.getName())
                .relevance(1)// 初始相關性分數為1
                .build())
        );
        maskList.forEach(mask -> {
            resultSet.add(ResSearchResult.builder()
                    .type("Mask")
                    .name(mask.getName())
                    .relevance(1)// 初始相關性分數為1
                    .build());
        }
        );

        resultSet.forEach(resSearchResult -> {
            MaskNamePojo maskNamePojo = null;
            if (resSearchResult.getType().equals("Mask")) {
                maskNamePojo = MaskNameParser.parse(resSearchResult.getName());
            }
            //第二階段使用equalsIgnoreCase，中了的話+1分
            if (resSearchResult.getType().equals("Pharmacy") && resSearchResult.getName().equalsIgnoreCase(keyword)) {
                resSearchResult.setRelevance(resSearchResult.getRelevance() + 1);
            } else if (resSearchResult.getType().equals("Mask")) {
                if (maskNamePojo.getName().equalsIgnoreCase(keyword)){
                    resSearchResult.setRelevance(resSearchResult.getRelevance() + 1);
                }
            }
            //第三階段使用equals，中了的話+1分
            if (resSearchResult.getType().equals("Pharmacy") && resSearchResult.getName().equals(keyword)) {
                resSearchResult.setRelevance(resSearchResult.getRelevance() + 1);
            } else if (resSearchResult.getType().equals("Mask")) {
                if (maskNamePojo.getName().equals(keyword)){
                    resSearchResult.setRelevance(resSearchResult.getRelevance() + 1);
                }
            }
        });

        //使用relevance進行排序
        List<ResSearchResult> sortedResults = new ArrayList<>(resultSet);
        return sortedResults.stream().sorted(Comparator.comparingInt(ResSearchResult::getRelevance).reversed()).toList();
    }

    /**
     * 購買口罩
     *
     * @param reqPurchase 購買請求
     */
    @Override
    @Transactional
    public void purchaseMask(ReqPurchase reqPurchase) {
        List<PurchaseHistory> purchaseHistories = new ArrayList<>(reqPurchase.getPurchaseMasks().stream().map(e -> {
            MaskNamePojo maskNamePojo = MaskNameParser.parse(e.getMaskName());
            return PurchaseHistory.builder()
                    .pharmacyName(e.getPharmacyName())
                    .maskName(e.getMaskName())
                    .maskColor(maskNamePojo.getColor())
                    .maskPackSize(maskNamePojo.getPackSize())
                    .build();
        }).toList());
        // 交易失敗情境
        // 1. 藥局沒庫存
        // 2. user不夠錢
        //----

        //1. 檢查庫存
        List<Pharmacies> pharmaciesList = pharmaciesRepository.findAllByNameIn(
                purchaseHistories.stream().map(PurchaseHistory::getPharmacyName).toList()
        );
        Map<String, Pharmacies> pharmaciesMap = pharmaciesList.stream().collect(
                HashMap::new,
                (map, pharmacy) -> map.put(pharmacy.getName(), pharmacy),
                HashMap::putAll
        );
        purchaseHistories.forEach(e->{
            Pharmacies pharmacies = pharmaciesMap.get(e.getPharmacyName());
            if (pharmacies == null) {
                throw new RuntimeException("Pharmacy not found: " + e.getPharmacyName());
            }
            Optional<Mask> optionalMask = pharmacies.getMasks().stream()
                    .filter(mask -> mask.getName().equals(e.getMaskName()))
                    .findFirst();
            if (optionalMask.isEmpty()) {
                throw new RuntimeException("Mask not found: " + e.getMaskName() + " in pharmacy: " + e.getPharmacyName());
            }
            Mask mask = optionalMask.get();
            // 設定交易金額與交易日期
            e.setTransactionAmount(mask.getPrice());
            e.setTransactionDate(LocalDateTime.now());
        });


        // 2. 檢查使用者夠不夠錢
        User user = userRepository.findByName(reqPurchase.getUserName()).orElseThrow(() -> new RuntimeException("User not found"));
        double totalAmount = purchaseHistories.stream().mapToDouble(PurchaseHistory::getTransactionAmount).sum();
        if (user.getCashBalance() < totalAmount) {
            throw new RuntimeException("Insufficient balance");
        }

        // 交易成功，更新資料庫
        // 1. 扣使用者錢
        user.setCashBalance(user.getCashBalance() - totalAmount);
        // 2. 紀錄購買紀錄
        user.getPurchaseHistories().addAll(purchaseHistories);
        userRepository.save(user);

        // 3. 加藥局錢
        pharmaciesMap.values().forEach(pharmacies -> {
            double amount = purchaseHistories.stream()
                    .filter(e -> e.getPharmacyName().equals(pharmacies.getName()))
                    .mapToDouble(PurchaseHistory::getTransactionAmount)
                    .sum();
            pharmacies.setCashBalance(pharmacies.getCashBalance() + amount);
        });
        pharmaciesRepository.saveAll(pharmaciesMap.values());
    }

    /**
     * 取得購買紀錄
     *
     * @param userName 使用者名稱
     * @return 購買紀錄
     */
    @Override
    public User getUser(String userName) {
        return userRepository.findByName(userName).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
