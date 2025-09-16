package com.example.phantommask.repository.repository;

import com.example.phantommask.param.ResPurchaseUser;
import com.example.phantommask.repository.entity.PurchaseHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {

    //使用DTO進行查詢，須加上countQuery
    @Query(
            value = "SELECT new com.example.phantommask.param.ResPurchaseUser(p.user.name, SUM(p.transactionAmount)) " +
            "FROM PurchaseHistory p " +
            "WHERE p.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.user " +
            "ORDER BY SUM(p.transactionAmount) DESC",
            countQuery = "SELECT COUNT(DISTINCT p.user) " +
                    "FROM PurchaseHistory p " +
                    "WHERE p.transactionDate BETWEEN :startDate AND :endDate"
    )
    List<ResPurchaseUser> findTopUsersByTotalAmount(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    Pageable pageable);

    List<PurchaseHistory> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
