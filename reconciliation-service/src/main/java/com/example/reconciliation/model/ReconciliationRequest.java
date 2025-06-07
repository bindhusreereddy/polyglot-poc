package com.example.reconciliation.model;

import java.time.LocalDateTime;

public class ReconciliationRequest {
    private String merchantId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Getters and Setters
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}