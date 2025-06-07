package com.example.reconciliation.repository;

import com.example.reconciliation.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByMerchantIdAndTransactionDateBetween(
            String merchantId, LocalDateTime startDate, LocalDateTime endDate);
}