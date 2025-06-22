package com.example.reconciliation.service;

import com.example.reconciliation.dto.ReconciliationRequest;
import com.example.reconciliation.dto.TransactionDto;
import com.example.reconciliation.entity.LedgerEntry;
import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.repository.LedgerEntryRepository;
import com.example.reconciliation.repository.SettlementReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ReconciliationService {
    private static final Logger logger = LoggerFactory.getLogger(ReconciliationService.class);
    private final TransactionServiceClient transactionServiceClient;
    private final SettlementReportRepository settlementReportRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public ReconciliationService(TransactionServiceClient transactionServiceClient,
                                 SettlementReportRepository settlementReportRepository,
                                 LedgerEntryRepository ledgerEntryRepository) {
        this.transactionServiceClient = transactionServiceClient;
        this.settlementReportRepository = settlementReportRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @Transactional
    public SettlementReport runReconciliation(ReconciliationRequest request) {
        logger.info("Starting reconciliation for merchant: {}, from: {}, to: {}",
                request.getMerchantId(), request.getStartDate(), request.getEndDate());

        // Hardcoded transaction IDs for POC (replace with dynamic source if needed)
        List<String> transactionIds = Arrays.asList("TXN001", "TXN002", "TXN003", "TXN004");

        // Fetch successful transactions by IDs
        List<TransactionDto> transactions = transactionServiceClient.getTransactionsByIds(transactionIds);

        // Calculate total amount for successful transactions
        BigDecimal totalAmount = transactions.stream()
                .map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create settlement report
        SettlementReport report = new SettlementReport();
        String reportId = UUID.randomUUID().toString();
        report.setReportId(reportId);
        report.setMerchantId(request.getMerchantId());
        report.setTotalAmount(totalAmount);
        report.setCurrency(transactions.isEmpty() ? "USD" : transactions.get(0).getCurrency()); // Assume same currency
        report.setStatus("SUCCESS");
        report.setCreatedAt(LocalDateTime.now());
        settlementReportRepository.save(report);

        // Create ledger entries for each transaction
        for (TransactionDto transaction : transactions) {
            LedgerEntry ledgerEntry = new LedgerEntry();
            ledgerEntry.setTransactionId(transaction.getId());
            ledgerEntry.setUserId(transaction.getUserId());
            ledgerEntry.setAmount(transaction.getAmount());
            ledgerEntry.setCurrency(transaction.getCurrency());
            ledgerEntry.setStatus(transaction.getStatus());
            ledgerEntry.setSenderAccount(transaction.getSenderAccount());
            ledgerEntry.setReceiverAccount(transaction.getReceiverAccount());
            ledgerEntry.setTimestamp(transaction.getTimestamp());
            ledgerEntry.setSettlementDate(LocalDate.now());
            ledgerEntry.setSettlementReportId(reportId);
            ledgerEntryRepository.save(ledgerEntry);
        }

        logger.info("Reconciliation completed. Report ID: {}, Total Amount: {}", reportId, totalAmount);
        return report;
    }
}