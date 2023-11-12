package com.example.pms.payment;

import com.example.pms.account.Account;
import com.example.pms.account.AccountRepository;
import com.example.pms.bank.Bank;
import com.example.pms.model.PaymentStatusEnum;
import com.example.pms.security.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final String sourceAccountNumber = "PK16MEZN0000000000001000";
    private final String targetAccountNumber = "PK16MEZN0000000000001001";
    private final UUID actualPaymentId = UUID.fromString("2ec9a4d1-a0cb-436f-9433-bd0cd40ba587");
    private final UUID dummyPaymentId = UUID.fromString("2ec9a4d1-a0cb-436f-9433-bd0cd40ba590");

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    private Payment setUp() {
        Bank bank = new Bank();
        bank.setId(1L);

        User user = new User();
        user.setId(1L);

        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber(sourceAccountNumber);
        sourceAccount.setHolderName("abc");
        sourceAccount.setCurrencyCode("PKR");
        sourceAccount.setBank(bank);
        sourceAccount.setUser(user);
        accountRepository.save(sourceAccount);

        Account targetAccount = new Account();
        targetAccount.setAccountNumber(targetAccountNumber);
        targetAccount.setHolderName("abc");
        targetAccount.setCurrencyCode("PKR");
        targetAccount.setBank(bank);
        targetAccount.setUser(user);
        accountRepository.save(targetAccount);

        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setSourceAccount(sourceAccount);
        payment.setTargetAccount(targetAccount);
        payment.setPaymentId(actualPaymentId);
        payment.setStatus(PaymentStatusEnum.SUCCESSFUL.getValue());
        paymentRepository.save(payment);

        return payment;
    }

    @Test
    public void testFindPaymentsWithFilters() {
        setUp();

        List<Payment> paymentList = paymentRepository.findPaymentsWithFilters(sourceAccountNumber, null, null);

        assertEquals(paymentList.size(), 1);
    }

    @Test
    public void testNotFindPaymentsWithFilters() {
        setUp();

        List<Payment> paymentList = paymentRepository.findPaymentsWithFilters(sourceAccountNumber, targetAccountNumber, PaymentStatusEnum.FAILED.getValue());

        assertEquals(paymentList.size(), 0);
    }

    @Test
    public void testFindByPaymentId () {
        Payment payment = setUp();

        Payment fetchedPayment = paymentRepository.findByPaymentId(actualPaymentId);

        assertEquals(payment, fetchedPayment);
    }

    @Test
    public void testNotFindByPaymentId () {
        setUp();

        Payment fetchedPayment = paymentRepository.findByPaymentId(dummyPaymentId);

        assertNull(fetchedPayment);
    }

    @Test
    public void testFindBySourceAccountNumber () {
        Payment payment = setUp();

        Payment fetchedPayment = paymentRepository.findByPaymentId(actualPaymentId);

        assertEquals(payment, fetchedPayment);
    }

    @Test
    public void testNotFindBySourceAccountNumber () {
        setUp();

        Payment fetchedPayment = paymentRepository.findByPaymentId(dummyPaymentId);

        assertNull(fetchedPayment);
    }
}