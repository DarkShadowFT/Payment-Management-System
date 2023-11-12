package com.example.pms.account;

import com.example.pms.bank.Bank;
import com.example.pms.security.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private final String accountNumber = "PK16MEZN0000000000001000";
    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    Account setUp() {
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

        return account;
    }

    @Test
    public void testExistsByAccountNumber() {
        setUp();

        boolean exists = accountRepository.existsByAccountNumber(accountNumber);

        assertTrue(exists);
    }

    @Test
    public void testNotExistsByAccountNumber() {
        setUp();

        boolean exists = accountRepository.existsByAccountNumber(accountNumber + "2");

        assertFalse(exists);
    }

    @Test
    public void testFindByAccountNumber() {
        Account account = setUp();

        Account fetchedAccount = accountRepository.findByAccountNumber(accountNumber);

        assertEquals(fetchedAccount, account);
    }

    @Test
    public void testNotFindByAccountNumber() {
        Account account = setUp();

        Account fetchedAccount = accountRepository.findByAccountNumber(accountNumber + "2");

        assertNotEquals(fetchedAccount, account);
    }
}
