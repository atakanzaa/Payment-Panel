package com.paymentpanel.service;
import com.paymentpanel.model.DailyStatistic;
import com.paymentpanel.model.Transaction;
import com.paymentpanel.dto.response.StatisticsResponse;
import com.paymentpanel.repository.DailyStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    
    @Autowired
    private DailyStatisticRepository dailyStatisticRepository;
    
    // Get statistics for a specific date
    public StatisticsResponse getStatisticsByDate(LocalDate date) {
        DailyStatistic dailyStatistic = dailyStatisticRepository.findByDate(date)
                .orElse(createEmptyStatistic(date));
        
        return mapToResponse(dailyStatistic);
    }
    
    // Get statistics for a date range
    public List<StatisticsResponse> getStatisticsForDateRange(LocalDate startDate, LocalDate endDate) {
        List<DailyStatistic> statistics = dailyStatisticRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        
        return statistics.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Update daily statistics for a new transaction
    @Transactional
    public void updateDailyStatisticForNewTransaction(Transaction transaction) {
        LocalDate date = transaction.getCreatedAt().toLocalDate();
        BigDecimal amount = transaction.getAmount();
        
        incrementPendingTransactionCount(date, amount);
    }
    
    // Increment pending transaction count
    @Transactional
    public void incrementPendingTransactionCount(LocalDate date, BigDecimal amount) {
        DailyStatistic dailyStatistic = dailyStatisticRepository.findByDate(date)
                .orElse(createEmptyStatistic(date));
        
        dailyStatistic.setTransactionCount(dailyStatistic.getTransactionCount() + 1);
        dailyStatistic.setPendingCount(dailyStatistic.getPendingCount() + 1);
        dailyStatistic.setTotalAmount(dailyStatistic.getTotalAmount().add(amount));
        
        dailyStatisticRepository.save(dailyStatistic);
    }
    
    // Update statistics for approved transaction
    @Transactional
    public void updateStatisticsForApprovedTransaction(LocalDate date, BigDecimal amount, Long processingTimeSeconds) {
        DailyStatistic dailyStatistic = dailyStatisticRepository.findByDate(date)
                .orElse(createEmptyStatistic(date));
        
        // Increment approved count, decrement pending count
        dailyStatistic.setApprovedCount(dailyStatistic.getApprovedCount() + 1);
        dailyStatistic.setPendingCount(Math.max(0, dailyStatistic.getPendingCount() - 1));
        
        // Update average processing time
        int currentTotal = dailyStatistic.getAverageProcessingTime() * (dailyStatistic.getApprovedCount() - 1);
        int newAverage = 0;
        
        if (dailyStatistic.getApprovedCount() > 0) {
            newAverage = (int) ((currentTotal + processingTimeSeconds) / dailyStatistic.getApprovedCount());
        }
        
        dailyStatistic.setAverageProcessingTime(newAverage);
        
        dailyStatisticRepository.save(dailyStatistic);
    }
    
    // Update daily statistics for an approved transaction
    @Transactional
    public void updateDailyStatisticForApprovedTransaction(Transaction transaction) {
        LocalDate date = transaction.getCreatedAt().toLocalDate();
        BigDecimal amount = transaction.getAmount();
        long processingTimeSeconds = 0;
        
        if (transaction.getApprovedAt() != null && transaction.getCreatedAt() != null) {
            processingTimeSeconds = java.time.Duration.between(transaction.getCreatedAt(), transaction.getApprovedAt()).getSeconds();
        }
        
        updateStatisticsForApprovedTransaction(date, amount, processingTimeSeconds);
    }
    
    // Update statistics for rejected transaction
    @Transactional
    public void updateStatisticsForRejectedTransaction(LocalDate date, BigDecimal amount, Long processingTimeSeconds) {
        DailyStatistic dailyStatistic = dailyStatisticRepository.findByDate(date)
                .orElse(createEmptyStatistic(date));
        
        // Increment rejected count, decrement pending count
        dailyStatistic.setRejectedCount(dailyStatistic.getRejectedCount() + 1);
        dailyStatistic.setPendingCount(Math.max(0, dailyStatistic.getPendingCount() - 1));
        
        dailyStatisticRepository.save(dailyStatistic);
    }
    
    // Update daily statistics for a rejected transaction
    @Transactional
    public void updateDailyStatisticForRejectedTransaction(Transaction transaction) {
        LocalDate date = transaction.getCreatedAt().toLocalDate();
        BigDecimal amount = transaction.getAmount();
        long processingTimeSeconds = 0;
        
        if (transaction.getRejectedAt() != null && transaction.getCreatedAt() != null) {
            processingTimeSeconds = java.time.Duration.between(transaction.getCreatedAt(), transaction.getRejectedAt()).getSeconds();
        }
        
        updateStatisticsForRejectedTransaction(date, amount, processingTimeSeconds);
    }
    
    // Helper methods
    private DailyStatistic createEmptyStatistic(LocalDate date) {
        DailyStatistic dailyStatistic = new DailyStatistic();
        dailyStatistic.setDate(date);
        dailyStatistic.setTransactionCount(0);
        dailyStatistic.setApprovedCount(0);
        dailyStatistic.setRejectedCount(0);
        dailyStatistic.setPendingCount(0);
        dailyStatistic.setTotalAmount(BigDecimal.ZERO);
        dailyStatistic.setAverageProcessingTime(0);
        
        return dailyStatistic;
    }
    
    private StatisticsResponse mapToResponse(DailyStatistic dailyStatistic) {
        StatisticsResponse response = new StatisticsResponse();
        response.setDate(dailyStatistic.getDate());
        response.setTransactionCount(dailyStatistic.getTransactionCount());
        response.setApprovedCount(dailyStatistic.getApprovedCount());
        response.setRejectedCount(dailyStatistic.getRejectedCount());
        response.setPendingCount(dailyStatistic.getPendingCount());
        response.setTotalAmount(dailyStatistic.getTotalAmount());
        response.setAverageProcessingTime(dailyStatistic.getAverageProcessingTime());
        
        return response;
    }
}
