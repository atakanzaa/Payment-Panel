package com.paymentpanel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Removed PaymentMethod relationship
    
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;
    
    @Column(name = "account_owner", nullable = false, length = 100)
    private String accountOwner;
    
    @Column(nullable = false, length = 50, unique = true)
    private String iban;
    
    @Column(name = "branch_code", length = 20)
    private String branchCode;
    
    @Column(name = "account_number", length = 50)
    private String accountNumber;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "logo_url")
    private String logoUrl;
    
    @Column(name = "min_limit")
    private BigDecimal minLimit;
    
    @Column(name = "max_limit")
    private BigDecimal maxLimit;
    
    @Column(length = 20)
    private String status = "ACTIVE";
    
    @Column(name = "team_code", length = 10)
    private String teamCode;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
