package com.example.reconciliation.repository;

import com.example.reconciliation.entity.SettlementReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementReportRepository extends JpaRepository<SettlementReport, Long> {
    SettlementReport findByReportId(String reportId);
}