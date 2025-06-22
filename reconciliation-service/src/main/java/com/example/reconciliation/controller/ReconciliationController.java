package com.example.reconciliation.controller;

import com.example.reconciliation.dto.ReconciliationRequest;
import com.example.reconciliation.entity.SettlementReport;
import com.example.reconciliation.service.ReconciliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {
    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping("/run")
    public ResponseEntity<SettlementReport> runReconciliation(@RequestBody ReconciliationRequest request) {
        SettlementReport report = reconciliationService.runReconciliation(request);
        return ResponseEntity.ok(report);
    }
}