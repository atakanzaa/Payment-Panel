package com.paymentpanel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.paymentpanel.model.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByInternalId(String internalId);
    
    Page<Transaction> findByStatus(String status, Pageable pageable);
    
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT t FROM Transaction t WHERE t.status = :status AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByStatusAndDateRange(String status, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status AND t.createdAt BETWEEN :startDate AND :endDate")
    Long countByStatusAndDateRange(String status, LocalDateTime startDate, LocalDateTime endDate);
}
