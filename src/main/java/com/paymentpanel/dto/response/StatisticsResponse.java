package com.paymentpanel.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StatisticsResponse {
    
    private LocalDate date;
    private Integer transactionCount;
    private Integer approvedCount;
    private Integer rejectedCount;
    private Integer pendingCount;
    private BigDecimal totalAmount;
    private Integer averageProcessingTime; // In seconds
}