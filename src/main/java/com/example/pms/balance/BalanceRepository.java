package com.example.pms.balance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Query("SELECT b from Balance b where b.account.accountNumber = :accountNumber")
    Balance findByAccountNumber(String accountNumber);

    @Query("SELECT b.amount from Balance b WHERE b.account.id = :accountId")
    BigDecimal findBalanceByAccountId(Long accountId);

    @Modifying
    @Query("UPDATE Balance b " +
            "SET b.amount = :newBalance, b.updatedAt = CURRENT_TIMESTAMP(), b.lastUpdatedPaymentId = :paymentId " +
            "WHERE b.account.id = :accountId")
    void updateAccountBalance(Long accountId, BigDecimal newBalance, UUID paymentId);
}
