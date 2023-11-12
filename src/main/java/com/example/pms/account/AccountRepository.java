package com.example.pms.account;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    Account findByAccountNumber(String accountNumber);
}
