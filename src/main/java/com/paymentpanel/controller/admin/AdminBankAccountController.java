package com.paymentpanel.controller.admin;

import com.paymentpanel.dto.request.BankAccountRequest;
import com.paymentpanel.dto.response.BankAccountResponse;
import com.paymentpanel.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bank-accounts")
@CrossOrigin(origins = "*")
public class AdminBankAccountController {
    
    @Autowired
    private BankAccountService bankAccountService;
    
    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllBankAccounts() {
        List<BankAccountResponse> bankAccounts = bankAccountService.getAllBankAccounts();
        return ResponseEntity.ok(bankAccounts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getBankAccountById(@PathVariable Long id) {
        BankAccountResponse bankAccount = bankAccountService.getBankAccountById(id);
        return ResponseEntity.ok(bankAccount);
    }
    
    @PostMapping
    public ResponseEntity<BankAccountResponse> createBankAccount(@Valid @RequestBody BankAccountRequest request) {
        BankAccountResponse bankAccount = bankAccountService.createBankAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccount);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponse> updateBankAccount(
            @PathVariable Long id, 
            @Valid @RequestBody BankAccountRequest request) {
        BankAccountResponse bankAccount = bankAccountService.updateBankAccount(id, request);
        return ResponseEntity.ok(bankAccount);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<BankAccountResponse> updateBankAccountStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        BankAccountResponse bankAccount = bankAccountService.updateBankAccountStatus(id, status);
        return ResponseEntity.ok(bankAccount);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }
}
