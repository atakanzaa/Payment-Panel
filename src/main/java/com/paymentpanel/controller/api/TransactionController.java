package com.paymentpanel.controller.api;

import com.paymentpanel.dto.request.TransactionRequest;
import com.paymentpanel.dto.response.TransactionResponse;
import com.paymentpanel.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import com.paymentpanel.dto.request.PaymentConfirmationRequest;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            HttpServletRequest servletRequest) {
        
        // Set IP and user agent
        request.setIpAddress(servletRequest.getRemoteAddr());
        request.setUserAgent(servletRequest.getHeader("User-Agent"));
        
        TransactionResponse transaction = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/tracking/{internalId}")
    public ResponseEntity<TransactionResponse> getTransactionByInternalId(@PathVariable String internalId) {
        TransactionResponse transaction = transactionService.getTransactionByInternalId(internalId);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUserId(@PathVariable Long userId) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}/payment-confirmation")
    public ResponseEntity<TransactionResponse> confirmPayment(@PathVariable Long id,
            @Valid @RequestBody PaymentConfirmationRequest request) {
        TransactionResponse updated = transactionService.confirmPaymentByUser(id, request);
        return ResponseEntity.ok(updated);
    }
}
