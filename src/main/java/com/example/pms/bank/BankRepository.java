package com.example.pms.bank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    boolean existsByBankId(String bankId);

    Bank findByBankId(String bankId);
}
