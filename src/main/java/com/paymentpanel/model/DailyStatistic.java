package com.paymentpanel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatistic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private LocalDate date;
    
    @Column(name = "transaction_count")
    private Integer transactionCount = 0;
    
    @Column(name = "approved_count")
    private Integer approvedCount = 0;
    
    @Column(name = "rejected_count")
    private Integer rejectedCount = 0;
    
    @Column(name = "pending_count")
    private Integer pendingCount = 0;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "average_processing_time")
    private Integer averageProcessingTime = 0;
    
    @Column(name = "team_code", length = 10)
    private String teamCode;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
