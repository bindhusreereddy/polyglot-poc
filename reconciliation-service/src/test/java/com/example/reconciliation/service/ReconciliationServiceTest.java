package com.example.reconciliation.service;

import com.example.reconciliation.dto.ReconciliationRequest;
import com.example.reconciliation.dto.TransactionDto;
import com.example.reconciliation.entity.LedgerEntry;
import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.repository.LedgerEntryRepository;
import com.example.reconciliation.repository.SettlementReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReconciliationServiceTest {

    @Autowired
    private ReconciliationService reconciliationService;

    @MockBean
    private TransactionServiceClient transactionServiceClient;

    @Autowired
    private SettlementReportRepository settlementReportRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @BeforeEach
    public void setUp() {
        assertNotNull(reconciliationService, "ReconciliationService should not be null");
        assertNotNull(settlementReportRepository, "SettlementReportRepository should not be null");
        assertNotNull(ledgerEntryRepository, "LedgerEntryRepository should not be null");

        TransactionDto txn1 = new TransactionDto();
        txn1.setId("TXN001");
        txn1.setAmount(new BigDecimal("100.00"));
        txn1.setStatus("SUCCESS");

        TransactionDto txn2 = new TransactionDto();
        txn2.setId("TXN002");
        txn2.setAmount(new BigDecimal("200.00"));
        txn2.setStatus("SUCCESS");

        TransactionDto txn3 = new TransactionDto();
        txn3.setId("TXN003");
        txn3.setAmount(new BigDecimal("150.00"));
        txn3.setStatus("FAILED");

        TransactionDto txn4 = new TransactionDto();
        txn4.setId("TXN004");
        txn4.setAmount(new BigDecimal("300.00"));
        txn4.setStatus("SUCCESS");

        when(transactionServiceClient.getTransactionsByIds(Arrays.asList("TXN001", "TXN002", "TXN003", "TXN004")))
                .thenReturn(Arrays.asList(txn1, txn2, txn3, txn4));

        when(settlementReportRepository.save(any(SettlementReport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(ledgerEntryRepository.save(any(LedgerEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testRunReconciliation() {
        ReconciliationRequest request = new ReconciliationRequest();
        request.setMerchantId("MERCH001");
        request.setStartDate(LocalDateTime.of(2025, 6, 6, 0, 0));
        request.setEndDate(LocalDateTime.of(2025, 6, 7, 0, 0));

        SettlementReport report = reconciliationService.runReconciliation(request);

        assertNotNull(report, "SettlementReport should not be null");
        assertEquals("MERCH001", report.getMerchantId());
        assertEquals(new BigDecimal("600.00"), report.getTotalAmount()); // 100 + 200 + 300
        assertEquals("SUCCESS", report.getStatus());
    }
}