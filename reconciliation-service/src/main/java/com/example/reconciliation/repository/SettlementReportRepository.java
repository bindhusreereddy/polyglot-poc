package com.example.reconciliation.repository;

import com.example.reconciliation.entity.SettlementReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementReportRepository extends JpaRepository<SettlementReport, Long> {
}