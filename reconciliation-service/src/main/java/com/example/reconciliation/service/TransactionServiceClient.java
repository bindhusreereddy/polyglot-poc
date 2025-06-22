package com.example.reconciliation.service;

import com.example.reconciliation.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class TransactionServiceClient {
    private final RestTemplate restTemplate;
    private final String transactionServiceUrl;

    public TransactionServiceClient(RestTemplate restTemplate, @Value("${transaction.service.url}") String transactionServiceUrl) {
        this.restTemplate = restTemplate;
        this.transactionServiceUrl = transactionServiceUrl;
    }

    public TransactionDto getTransactionById(String id) {
        String url = UriComponentsBuilder.fromHttpUrl(transactionServiceUrl + "/api/transactions/{id}")
                .buildAndExpand(id)
                .toUriString();
        return restTemplate.getForObject(url, TransactionDto.class);
    }

    public List<TransactionDto> getTransactionsByIds(List<String> ids) {
        return ids.stream()
                .map(this::getTransactionById)
                .filter(dto -> dto != null && "SUCCESS".equalsIgnoreCase(dto.getStatus()))
                .toList();
    }
}