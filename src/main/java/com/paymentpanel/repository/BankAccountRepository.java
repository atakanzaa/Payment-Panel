package com.paymentpanel.repository;
import com.paymentpanel.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    
    // Removed methods related to PaymentMethod
    
    List<BankAccount> findByStatusOrderByBankNameAsc(String status);
    
    List<BankAccount> findAllByStatus(String status);
}
