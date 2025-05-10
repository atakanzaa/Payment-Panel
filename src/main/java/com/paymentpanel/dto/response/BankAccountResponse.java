package com.paymentpanel.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BankAccountResponse {
    
    private Long id;
    private String bankName;
    private String accountOwner;
    private String iban;
    private String branchCode;
    private String accountNumber;
    private String description;
    private String logoUrl;
    private BigDecimal minLimit;
    private BigDecimal maxLimit;
    private String status;
    private String teamCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
