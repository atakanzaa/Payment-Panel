package com.paymentpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionStatusUpdateRequest {
    
    @NotBlank(message = "Status is required")
    private String status; // APPROVED, REJECTED
    
    private String note;
    
    @NotBlank(message = "Admin username is required")
    private String adminUsername;
}
