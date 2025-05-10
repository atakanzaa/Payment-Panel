package com.paymentpanel.service;

import com.paymentpanel.exception.ResourceNotFoundException;
import com.paymentpanel.exception.ValidationException;
import com.paymentpanel.model.BankAccount;
import com.paymentpanel.model.Transaction;
import com.paymentpanel.model.User;
import com.paymentpanel.repository.BankAccountRepository;
import com.paymentpanel.repository.TransactionRepository;
import com.paymentpanel.repository.UserRepository;
import com.paymentpanel.dto.request.TransactionRequest;
import com.paymentpanel.dto.response.TransactionResponse;
import com.paymentpanel.dto.request.PaymentConfirmationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StatisticsService statisticsService;
    
    // Create a new transaction
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        // First get the bank account since it's always required
        BankAccount bankAccount = bankAccountRepository.findById(request.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + request.getBankAccountId()));
        
        if (!"ACTIVE".equals(bankAccount.getStatus())) {
            throw new ValidationException("Bank account is not active");
        }
        
        // Check bank-specific limits if set
        if (bankAccount.getMinLimit() != null && request.getAmount().compareTo(bankAccount.getMinLimit()) < 0) {
            throw new ValidationException("Amount is below the bank minimum limit: " + bankAccount.getMinLimit());
        }
        
        if (bankAccount.getMaxLimit() != null && request.getAmount().compareTo(bankAccount.getMaxLimit()) > 0) {
            throw new ValidationException("Amount exceeds the bank maximum limit: " + bankAccount.getMaxLimit());
        }
        
        // Find or create user if user ID is provided
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        }
        
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBankAccount(bankAccount);
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus("PENDING");
        transaction.setIpAddress(request.getIpAddress());
        transaction.setUserAgent(request.getUserAgent());
        
        // Generate a unique internal ID
        transaction.setInternalId(generateUniqueTransactionId());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update daily statistics
        statisticsService.updateDailyStatisticForNewTransaction(savedTransaction);
        
        return mapToResponse(savedTransaction);
    }
    
    // Get transaction by ID
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        return mapToResponse(transaction);
    }
    
    // Get transaction by internal ID
    public TransactionResponse getTransactionByInternalId(String internalId) {
        Transaction transaction = transactionRepository.findByInternalId(internalId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with internal id: " + internalId));
        
        return mapToResponse(transaction);
    }
    
    // Get all transactions with pagination
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        
        return transactions.map(this::mapToResponse);
    }
    
    // Get transactions by status with pagination
    public Page<TransactionResponse> getTransactionsByStatus(String status, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByStatus(status, pageable);
        
        return transactions.map(this::mapToResponse);
    }
    
    // Get transactions by user ID
    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Approve transaction
    @Transactional
    public TransactionResponse approveTransaction(Long id, String approvedBy, String note) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        if (!"PENDING".equals(transaction.getStatus())) {
            throw new ValidationException("Transaction cannot be approved because it's not in PENDING status");
        }
        
        transaction.setStatus("APPROVED");
        transaction.setApprovedAt(LocalDateTime.now());
        transaction.setApprovedBy(approvedBy);
        
        if (note != null && !note.trim().isEmpty()) {
            transaction.setNote(note);
        }
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Update daily statistics
        statisticsService.updateDailyStatisticForApprovedTransaction(updatedTransaction);
        
        return mapToResponse(updatedTransaction);
    }
    
    // Reject transaction
    @Transactional
    public TransactionResponse rejectTransaction(Long id, String rejectedBy, String note) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        if (!"PENDING".equals(transaction.getStatus())) {
            throw new ValidationException("Transaction cannot be rejected because it's not in PENDING status");
        }
        
        transaction.setStatus("REJECTED");
        transaction.setRejectedAt(LocalDateTime.now());
        transaction.setRejectedBy(rejectedBy);
        
        if (note != null && !note.trim().isEmpty()) {
            transaction.setNote(note);
        }
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Update daily statistics
        statisticsService.updateDailyStatisticForRejectedTransaction(updatedTransaction);
        
        return mapToResponse(updatedTransaction);
    }

    @Transactional
    public TransactionResponse confirmPaymentByUser(Long id, PaymentConfirmationRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        // Persist payment confirmation fields
        transaction.setPaymentConfirmed(request.getPaymentConfirmed());
        transaction.setPaymentDate(request.getPaymentDate());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }
    
    // Helper methods
    private String generateUniqueTransactionId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
    
    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setInternalId(transaction.getInternalId());
        
        if (transaction.getUser() != null) {
            response.setUserId(transaction.getUser().getId());
            response.setUserName(transaction.getUser().getUsername());
        }
        
        response.setBankAccountId(transaction.getBankAccount().getId());
        response.setBankName(transaction.getBankAccount().getBankName());
        response.setAccountOwner(transaction.getBankAccount().getAccountOwner());
        response.setIban(transaction.getBankAccount().getIban());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setApprovedAt(transaction.getApprovedAt());
        response.setApprovedBy(transaction.getApprovedBy());
        response.setRejectedAt(transaction.getRejectedAt());
        response.setRejectedBy(transaction.getRejectedBy());
        response.setNote(transaction.getNote());
        
        // Calculate processing time
        if ((transaction.getApprovedAt() != null || transaction.getRejectedAt() != null) && transaction.getCreatedAt() != null) {
            LocalDateTime endTime = transaction.getApprovedAt() != null ? transaction.getApprovedAt() : transaction.getRejectedAt();
            Duration duration = Duration.between(transaction.getCreatedAt(), endTime);
            response.setProcessingTimeSeconds(duration.getSeconds());
        }
        
        return response;
    }
}
