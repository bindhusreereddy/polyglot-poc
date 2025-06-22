package com.example.reconciliation.controller;

import com.example.reconciliation.entity.LedgerEntry;
import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.model.ReconciliationRequest;
import com.example.reconciliation.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReconciliationController {

    //reconciliation controller
    @Autowired
    private ReconciliationService reconciliationService;

    @PostMapping("/reconciliation/run")
    public ResponseEntity<SettlementReport> runReconciliation(@RequestBody ReconciliationRequest request) {
        SettlementReport report = reconciliationService.runReconciliation(request);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/settlements/reports/{reportId}")
    public ResponseEntity<SettlementReport> getSettlementReport(@PathVariable String reportId) {
        SettlementReport report = reconciliationService.getSettlementReport(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @GetMapping("/ledger/merchant/{merchantId}")
    public ResponseEntity<List<LedgerEntry>> getLedgerEntries(@PathVariable String merchantId) {
        List<LedgerEntry> entries = reconciliationService.getLedgerEntries(merchantId);
        return ResponseEntity.ok(entries);
    }
}