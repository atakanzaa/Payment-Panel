package com.paymentpanel.service;

import com.paymentpanel.exception.ResourceNotFoundException;
import com.paymentpanel.model.BankAccount;
import com.paymentpanel.repository.BankAccountRepository;
import com.paymentpanel.dto.request.BankAccountRequest;
import com.paymentpanel.dto.response.BankAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    // Get all bank accounts
    public List<BankAccountResponse> getAllBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        
        return bankAccounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Get all active bank accounts for public API
    public List<BankAccountResponse> getAllActiveBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findByStatusOrderByBankNameAsc("ACTIVE");
        
        return bankAccounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Get bank account by id
    public BankAccountResponse getBankAccountById(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));
        
        return mapToResponse(bankAccount);
    }
    
    // Create new bank account
    @Transactional
    public BankAccountResponse createBankAccount(BankAccountRequest request) {
        BankAccount bankAccount = new BankAccount();
        mapRequestToEntity(request, bankAccount);
        
        BankAccount savedAccount = bankAccountRepository.save(bankAccount);
        
        return mapToResponse(savedAccount);
    }
    
    // Update bank account
    @Transactional
    public BankAccountResponse updateBankAccount(Long id, BankAccountRequest request) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));
        
        mapRequestToEntity(request, bankAccount);
        
        BankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        
        return mapToResponse(updatedAccount);
    }
    
    // Update bank account status
    @Transactional
    public BankAccountResponse updateBankAccountStatus(Long id, String status) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));
        
        bankAccount.setStatus(status);
        
        BankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        
        return mapToResponse(updatedAccount);
    }
    
    // Delete bank account
    @Transactional
    public void deleteBankAccount(Long id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bank account not found with id: " + id);
        }
        
        bankAccountRepository.deleteById(id);
    }
    
    // Helper methods
    private void mapRequestToEntity(BankAccountRequest request, BankAccount bankAccount) {
        bankAccount.setBankName(request.getBankName());
        bankAccount.setAccountOwner(request.getAccountOwner());
        bankAccount.setIban(request.getIban());
        bankAccount.setBranchCode(request.getBranchCode());
        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setDescription(request.getDescription());
        bankAccount.setLogoUrl(request.getLogoUrl());
        bankAccount.setMinLimit(request.getMinLimit());
        bankAccount.setMaxLimit(request.getMaxLimit());
        bankAccount.setStatus(request.getStatus());
        bankAccount.setTeamCode(request.getTeamCode());
    }
    
    private BankAccountResponse mapToResponse(BankAccount bankAccount) {
        BankAccountResponse response = new BankAccountResponse();
        response.setId(bankAccount.getId());
        response.setBankName(bankAccount.getBankName());
        response.setAccountOwner(bankAccount.getAccountOwner());
        response.setIban(bankAccount.getIban());
        response.setBranchCode(bankAccount.getBranchCode());
        response.setAccountNumber(bankAccount.getAccountNumber());
        response.setDescription(bankAccount.getDescription());
        response.setLogoUrl(bankAccount.getLogoUrl());
        response.setMinLimit(bankAccount.getMinLimit());
        response.setMaxLimit(bankAccount.getMaxLimit());
        response.setStatus(bankAccount.getStatus());
        response.setTeamCode(bankAccount.getTeamCode());
        response.setCreatedAt(bankAccount.getCreatedAt());
        response.setUpdatedAt(bankAccount.getUpdatedAt());
        
        return response;
    }
}
