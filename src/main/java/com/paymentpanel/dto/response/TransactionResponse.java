package com.paymentpanel.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    
    private Long id;
    private String internalId;
    private Long userId;
    private String userName;
    // Removed paymentMethodId and paymentMethodName
    private Long bankAccountId;
    private String bankName;
    private String accountOwner;
    private String iban;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String approvedBy;
    private LocalDateTime rejectedAt;
    private String rejectedBy;
    private String note;
    private Long processingTimeSeconds;
    private Boolean paymentConfirmed;
    private LocalDateTime paymentDate;
}
