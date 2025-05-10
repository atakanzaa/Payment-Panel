package com.paymentpanel.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountRequest {
    
    // Removed PaymentMethod ID field
    
    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must be less than 100 characters")
    private String bankName;
    
    @NotBlank(message = "Account owner is required")
    @Size(max = 100, message = "Account owner must be less than 100 characters")
    private String accountOwner;
    
    @NotBlank(message = "IBAN is required")
    @Size(max = 50, message = "IBAN must be less than 50 characters")
    private String iban;
    
    @Size(max = 20, message = "Branch code must be less than 20 characters")
    private String branchCode;
    
    @Size(max = 50, message = "Account number must be less than 50 characters")
    private String accountNumber;
    
    private String description;
    
    private String logoUrl;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum limit must be greater than or equal to 0")
    private BigDecimal minLimit;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum limit must be greater than or equal to 0")
    private BigDecimal maxLimit;
    
    private String status = "ACTIVE";
    
    private String teamCode;
}
