package com.paymentpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionStatusRequest {
    
    @NotBlank(message = "Action by is required")
    private String actionBy;
    
    private String note;
}