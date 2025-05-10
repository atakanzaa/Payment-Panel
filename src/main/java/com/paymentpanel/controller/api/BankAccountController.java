package com.paymentpanel.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paymentpanel.dto.response.BankAccountResponse;
import com.paymentpanel.service.BankAccountService;
import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@CrossOrigin(origins = "*")
public class BankAccountController {
    
    @Autowired
    private BankAccountService bankAccountService;
    
    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> getAllActiveBankAccounts() {
        // Get all active bank accounts for the public API
        List<BankAccountResponse> bankAccounts = bankAccountService.getAllActiveBankAccounts();
        return ResponseEntity.ok(bankAccounts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getBankAccountById(@PathVariable Long id) {
        BankAccountResponse bankAccount = bankAccountService.getBankAccountById(id);
        return ResponseEntity.ok(bankAccount);
    }
}
