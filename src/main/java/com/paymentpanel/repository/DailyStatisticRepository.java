package com.paymentpanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.paymentpanel.model.DailyStatistic;

@Repository
public interface DailyStatisticRepository extends JpaRepository<DailyStatistic, Long> {
    
    Optional<DailyStatistic> findByDate(LocalDate date);
    
    List<DailyStatistic> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
}
