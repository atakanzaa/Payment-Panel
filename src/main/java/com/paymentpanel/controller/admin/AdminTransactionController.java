package com.paymentpanel.controller.admin;

import com.paymentpanel.dto.request.TransactionStatusRequest;
import com.paymentpanel.dto.response.TransactionResponse;
import com.paymentpanel.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
@CrossOrigin(origins = "*")
public class AdminTransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, dir, sort);
        
        Page<TransactionResponse> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, dir, sort);
        
        Page<TransactionResponse> transactions = transactionService.getTransactionsByStatus(status.toUpperCase(), pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<TransactionResponse> approveTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionStatusRequest request) {
        
        TransactionResponse transaction = transactionService.approveTransaction(
                id, request.getActionBy(), request.getNote());
        
        return ResponseEntity.ok(transaction);
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<TransactionResponse> rejectTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionStatusRequest request) {
        
        TransactionResponse transaction = transactionService.rejectTransaction(
                id, request.getActionBy(), request.getNote());
        
        return ResponseEntity.ok(transaction);
    }
}
