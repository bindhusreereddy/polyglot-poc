package com.example.reconciliation.service;

import com.example.reconciliation.entity.LedgerEntry;
import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.entity.TransactionLog;
import com.example.reconciliation.model.ReconciliationRequest;
import com.example.reconciliation.repository.LedgerEntryRepository;
import com.example.reconciliation.repository.SettlementReportRepository;
import com.example.reconciliation.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReconciliationService {
    private static final Logger logger = LoggerFactory.getLogger(ReconciliationService.class);

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Autowired
    private SettlementReportRepository settlementReportRepository;

    @Transactional
    public SettlementReport runReconciliation(ReconciliationRequest request) {
        logger.info("Starting reconciliation for merchant: {}, from: {}, to: {}",
                request.getMerchantId(), request.getStartDate(), request.getEndDate());

        // Fetch transactions for the given merchant and date range
        List<TransactionLog> transactions = transactionLogRepository.findByMerchantIdAndTransactionDateBetween(
                request.getMerchantId(), request.getStartDate(), request.getEndDate());

        // Calculate total amount for successful transactions
        BigDecimal totalAmount = transactions.stream()
                .filter(t -> "SUCCESS".equalsIgnoreCase(t.getStatus()))
                .map(TransactionLog::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create settlement report
        SettlementReport report = new SettlementReport();
        String reportId = UUID.randomUUID().toString();
        report.setReportId(reportId);
        report.setMerchantId(request.getMerchantId());
        report.setTotalAmount(totalAmount);
        report.setStatus("COMPLETED");
        report.setCreatedAt(LocalDateTime.now());
        settlementReportRepository.save(report);

        // Create ledger entry
        LedgerEntry ledgerEntry = new LedgerEntry();
        ledgerEntry.setMerchantId(request.getMerchantId());
        ledgerEntry.setSettledAmount(totalAmount);
        ledgerEntry.setSettlementDate(LocalDate.now());
        ledgerEntry.setSettlementReportId(reportId);
        ledgerEntryRepository.save(ledgerEntry);

        logger.info("Reconciliation completed. Report ID: {}, Total Amount: {}", reportId, totalAmount);
        return report;
    }

    public SettlementReport getSettlementReport(String reportId) {
        return settlementReportRepository.findByReportId(reportId);
    }

    public List<LedgerEntry> getLedgerEntries(String merchantId) {
        return ledgerEntryRepository.findByMerchantId(merchantId);
    }
}