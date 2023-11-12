package com.example.pms.balance;

import com.example.pms.account.Account;
import com.example.pms.account.AccountRepository;
import com.example.pms.bank.Bank;
import com.example.pms.security.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final String accountNumber = "PK16MEZN0000000000001000";
    private final Long dummyAccountId = 10L;
    private final double delta = 0.00001;
    private final UUID paymentId = UUID.fromString("2ec9a4d1-a0cb-436f-9433-bd0cd40ba587");
    BigDecimal newBalance = BigDecimal.valueOf(100);

    @AfterEach
    void tearDown() {
        balanceRepository.deleteAll();
    }

    Balance setUp() {
        Bank bank = new Bank();
        bank.setId(1L);

        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setHolderName("abc");
        account.setCurrencyCode("PKR");
        account.setBank(bank);
        account.setUser(user);
        accountRepository.save(account);

        Balance balance = new Balance();
        balance.setAmount(BigDecimal.TEN);
        balance.setAccount(account);
        balanceRepository.save(balance);

        return balance;
    }

    @Test
    void testFindByAccountNumber() {
        Balance balance = setUp();

        Balance fetchedBalance = balanceRepository.findByAccountNumber(accountNumber);

        assertEquals(fetchedBalance, balance);
    }

    @Test
    void testNotFindByAccountNumber() {
        Balance balance = setUp();

        Balance fetchedBalance = balanceRepository.findByAccountNumber(accountNumber);

        assertEquals(fetchedBalance, balance);
    }

    @Test
    void testFindBalanceByAccountId() {
        Balance balance = setUp();
        BigDecimal balanceAmount = balance.getAmount();

        BigDecimal fetchedBalanceAmount = balanceRepository.findBalanceByAccountId(balance.getAccount().getId());

        assertEquals(fetchedBalanceAmount.doubleValue(), balanceAmount.doubleValue(), delta);
    }

    @Test
    void testNotFindBalanceByAccountId() {
        setUp();

        BigDecimal fetchedBalanceAmount = balanceRepository.findBalanceByAccountId(dummyAccountId);

        assertNull(fetchedBalanceAmount);
    }

    @Test
    void testUpdateAccountBalance() {
        Balance balance = setUp();
        Long accountId = balance.getAccount().getId();

        balanceRepository.updateAccountBalance(accountId, newBalance, paymentId);
        BigDecimal updatedBalance = balanceRepository.findBalanceByAccountId(accountId);

        assertEquals(updatedBalance.doubleValue(), newBalance.doubleValue(), delta);
    }

    @Test
    void testNotUpdateAccountBalance() {
        setUp();

        balanceRepository.updateAccountBalance(dummyAccountId, newBalance, paymentId);
        BigDecimal updatedBalance = balanceRepository.findBalanceByAccountId(dummyAccountId);

        assertNull(updatedBalance);
    }
}