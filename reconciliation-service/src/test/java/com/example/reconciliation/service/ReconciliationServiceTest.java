package com.example.reconciliation.service;

import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.entity.TransactionLog;
import com.example.reconciliation.model.ReconciliationRequest;
import com.example.reconciliation.repository.LedgerEntryRepository;
import com.example.reconciliation.repository.SettlementReportRepository;
import com.example.reconciliation.repository.TransactionLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReconciliationServiceTest {

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @Mock
    private SettlementReportRepository settlementReportRepository;

    @InjectMocks
    private ReconciliationService reconciliationService;

    @Test
    void testRunReconciliation() {
        // Arrange
        ReconciliationRequest request = new ReconciliationRequest();
        request.setMerchantId("MERCH001");
        request.setStartDate(LocalDateTime.of(2025, 6, 6, 0, 0));
        request.setEndDate(LocalDateTime.of(2025, 6, 7, 0, 0));

        TransactionLog txn1 = new TransactionLog();
        txn1.setAmount(new BigDecimal("100.00"));
        txn1.setStatus("SUCCESS");
        TransactionLog txn2 = new TransactionLog();
        txn2.setAmount(new BigDecimal("200.00"));
        txn2.setStatus("SUCCESS");
        TransactionLog txn3 = new TransactionLog();
        txn3.setAmount(new BigDecimal("150.00"));
        txn3.setStatus("FAILED");

        List<TransactionLog> transactions = Arrays.asList(txn1, txn2, txn3);
        when(transactionLogRepository.findByMerchantIdAndTransactionDateBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        when(settlementReportRepository.save(any(SettlementReport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(ledgerEntryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SettlementReport report = reconciliationService.runReconciliation(request);

        // Assert
        assertEquals(new BigDecimal("300.00"), report.getTotalAmount());
        assertEquals("COMPLETED", report.getStatus());
        verify(transactionLogRepository, times(1))
                .findByMerchantIdAndTransactionDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(settlementReportRepository, times(1)).save(any(SettlementReport.class));
        verify(ledgerEntryRepository, times(1)).save(any());
    }
}