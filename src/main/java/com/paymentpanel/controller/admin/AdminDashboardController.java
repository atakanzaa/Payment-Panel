package com.paymentpanel.controller.admin;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.paymentpanel.dto.response.StatisticsResponse;
import com.paymentpanel.service.StatisticsService;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping("/statistics/today")
    public ResponseEntity<StatisticsResponse> getTodayStatistics() {
        StatisticsResponse statistics = statisticsService.getStatisticsByDate(LocalDate.now());
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/statistics/date/{date}")
    public ResponseEntity<StatisticsResponse> getStatisticsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        StatisticsResponse statistics = statisticsService.getStatisticsByDate(date);
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/statistics/range")
    public ResponseEntity<List<StatisticsResponse>> getStatisticsForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StatisticsResponse> statistics = statisticsService.getStatisticsForDateRange(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
}
