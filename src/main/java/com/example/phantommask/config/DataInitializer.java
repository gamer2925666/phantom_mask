package com.example.phantommask.config;

import com.example.phantommask.param.MaskDto;
import com.example.phantommask.param.MaskNamePojo;
import com.example.phantommask.param.PharmaciesDto;
import com.example.phantommask.param.UserDto;
import com.example.phantommask.repository.entity.Mask;
import com.example.phantommask.repository.entity.PurchaseHistory;
import com.example.phantommask.repository.repository.PharmaciesRepository;
import com.example.phantommask.repository.repository.UserRepository;
import com.example.phantommask.repository.entity.Pharmacies;
import com.example.phantommask.repository.entity.User;
import com.example.phantommask.utils.MaskNameParser;
import com.example.phantommask.utils.OpeningHoursParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * 啟動時初始化資料庫
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PharmaciesRepository pharmaciesRepository;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    public DataInitializer(PharmaciesRepository pharmaciesRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.pharmaciesRepository = pharmaciesRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {

        // 匯入 pharmacies.json
        InputStream pharmaciesStream = getClass().getResourceAsStream("/data/pharmacies.json");
        List<PharmaciesDto> pharmaciesDtos = objectMapper.readValue(pharmaciesStream, new TypeReference<>() {});
        List<Pharmacies> pharmacies  = pharmaciesDtos.stream().map(pharmacy -> Pharmacies.builder()
                .name(pharmacy.getName())
                .cashBalance(pharmacy.getCashBalance())
                .openingHours(OpeningHoursParser.parse(pharmacy.getOpeningHours()))
                .masks(pharmacy.getMasks().stream().map(maskDto->{
                    Mask m = new Mask();
                    MaskNamePojo maskNamePojo = MaskNameParser.parse(maskDto.getName());
                    m.setName(maskDto.getName());
                    m.setColor(maskNamePojo.getColor());
                    m.setPackSize(maskNamePojo.getPackSize());
                    m.setPrice(maskDto.getPrice());
                    return m;
                }).toList())
                .build()).toList();
        pharmaciesRepository.saveAll(pharmacies);

        // 匯入 users.json
        InputStream usersStream = getClass().getResourceAsStream("/data/users.json");
        List<UserDto> userDtos = objectMapper.readValue(usersStream, new TypeReference<>() {});
        List<User> users = userDtos.stream().map(userDto -> User.builder()
                .cashBalance(userDto.getCashBalance())
                .name(userDto.getName())
                .purchaseHistories(userDto.getPurchaseHistories().stream().map(purchaseHistoryDto -> {
                    MaskNamePojo maskNamePojo = MaskNameParser.parse(purchaseHistoryDto.getMaskName());
                    return PurchaseHistory.builder()
                            .pharmacyName(purchaseHistoryDto.getPharmacyName())
                            .maskName(purchaseHistoryDto.getMaskName())
                            .maskColor(maskNamePojo.getColor())
                            .maskPackSize(maskNamePojo.getPackSize())
                            .transactionAmount(purchaseHistoryDto.getTransactionAmount())
                            .transactionDate(purchaseHistoryDto.getTransactionDate())
                            .build();
                }).toList())
                .build()).toList();
        userRepository.saveAll(users);

        log.info("資料已成功匯入資料庫。");
    }
}
