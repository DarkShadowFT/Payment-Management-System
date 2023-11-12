package com.example.pms.payment;

import com.example.pms.account.Account;
import com.example.pms.account.AccountRepository;
import com.example.pms.balance.BalanceRepository;
import com.example.pms.conversion.rate.ConversionRateRepository;
import com.example.pms.exception.ApiRequestException;
import com.example.pms.model.*;
import com.example.pms.security.user.User;
import com.example.pms.security.user.UserRepository;
import com.example.pms.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.pms.account.AccountService.ADMIN_ROLE;
import static com.example.pms.account.AccountService.USER_ROLE;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final ConversionRateRepository conversionRateRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    private static final String PAYMENT_NOT_FOUND_ERROR_MESSAGE = "Payment id does not exist!";
    private static final String INVALID_ACCOUNT_ERROR_MESSAGE = "Either sourceAccountNumber or targetAccountNumber does not exist!";
    private static final String INVALID_PAYMENT_SAME_ACCOUNT_ERROR_MESSAGE = "Payment cannot be sent to the same person!";
    private static final String INVALID_PAYMENT_INSUFFICIENT_FUNDS_ERROR_MESSAGE = "Insufficient funds to make payment!";

    @Autowired
    public PaymentService(
            PaymentRepository paymentRepository,
            AccountRepository accountRepository,
            ConversionRateRepository conversionRateRepository,
            BalanceRepository balanceRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.conversionRateRepository = conversionRateRepository;
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    UUID createPayment(PaymentRequest paymentRequest) {
        validatePaymentRequest(paymentRequest);
        Account sourceAccount = accountRepository.findByAccountNumber(paymentRequest.getSourceAccountNumber());
        SecurityUtils.performAccountAuthorization(sourceAccount.getUser().getUsername());

        Account targetAccount = accountRepository.findByAccountNumber(paymentRequest.getTargetAccountNumber());

        if (!isSufficientBalance(sourceAccount.getId(), paymentRequest.getAmount())) {
            createFailedPayment(sourceAccount, targetAccount, paymentRequest.getAmount());
            throw new ApiRequestException(INVALID_PAYMENT_INSUFFICIENT_FUNDS_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        Payment createdPayment = executePayment(sourceAccount, targetAccount, paymentRequest.getAmount());
        return createdPayment.getPaymentId();
    }

    private void validatePaymentRequest(PaymentRequest paymentRequest){
        if (paymentRequest.getSourceAccountNumber().equals(paymentRequest.getTargetAccountNumber())){
            throw new ApiRequestException(INVALID_PAYMENT_SAME_ACCOUNT_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        if (!accountRepository.existsByAccountNumber(paymentRequest.getSourceAccountNumber()) ||
                !accountRepository.existsByAccountNumber(paymentRequest.getTargetAccountNumber())){
            throw new ApiRequestException(INVALID_ACCOUNT_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
    }

    private BigDecimal convertAmount(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        if (!sourceCurrency.equals(targetCurrency)) {
            BigDecimal conversionRate = conversionRateRepository.getConversionRate(sourceCurrency, targetCurrency);
            return amount.multiply(conversionRate);
        }
        return amount;
    }

    private boolean isSufficientBalance(Long accountId, BigDecimal amount) {
        return balanceRepository.findBalanceByAccountId(accountId).compareTo(amount) >= 0;
    }

    private void createFailedPayment(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setSourceAccount(sourceAccount);
        payment.setTargetAccount(targetAccount);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatusEnum.FAILED.getValue());
        paymentRepository.save(payment);
    }

    private Payment executePayment(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        PaymentStatusEnum paymentStatus = PaymentStatusEnum.PENDING;
        Payment payment = new Payment();
        payment.setSourceAccount(sourceAccount);
        payment.setTargetAccount(targetAccount);
        payment.setAmount(amount);
        payment.setStatus(paymentStatus.getValue());
        Payment createdPayment = paymentRepository.save(payment);

        BigDecimal convertedAmount = convertAmount(sourceAccount.getCurrencyCode(),
                targetAccount.getCurrencyCode(), amount);

        BigDecimal sourceAccountOldBalance = balanceRepository.findBalanceByAccountId(sourceAccount.getId());
        BigDecimal targetAccountOldBalance = balanceRepository.findBalanceByAccountId(targetAccount.getId());
        BigDecimal sourceAccountNewBalance = sourceAccountOldBalance.subtract(amount);
        BigDecimal targetAccountNewBalance = targetAccountOldBalance.add(convertedAmount);
        balanceRepository.updateAccountBalance(sourceAccount.getId(), sourceAccountNewBalance, payment.getPaymentId());
        balanceRepository.updateAccountBalance(targetAccount.getId(), targetAccountNewBalance, payment.getPaymentId());

        paymentStatus = PaymentStatusEnum.SUCCESSFUL;
        createdPayment.setStatus(paymentStatus.getValue());
        return paymentRepository.save(createdPayment);
    }

    PaymentsResponse getPayments(String sourceAccountNumber, String targetAccountNumber, PaymentStatusEnum status) {
        List<Payment> paymentList;

        if (status != null) {
            paymentList = paymentRepository.findPaymentsWithFilters(sourceAccountNumber, targetAccountNumber,
                    status.getValue());
        } else {
            paymentList = paymentRepository.findPaymentsWithFilters(sourceAccountNumber, targetAccountNumber, null);
        }

        List<Payment> filteredPayments = new ArrayList<>();
        String username = SecurityUtils.extractUsername();
        User user = userRepository.findByUsername(username);

        for (Payment payment: paymentList) {
            if (SecurityUtils.isRole(user, ADMIN_ROLE)) {
                filteredPayments.add(payment);
            }
            else if (SecurityUtils.isRole(user, USER_ROLE) && payment.getSourceAccount().getUser().getUsername().equals(username)) {
                filteredPayments.add(payment);
            }
        }

        return PaymentMapper.mapToPaymentsResponse(filteredPayments);
    }

    PaymentResponse getPayment(UUID paymentId) {
        Payment payment = getExistingPayment(paymentId);
        SecurityUtils.performPaymentAuthorization(payment.getSourceAccount(), payment.getTargetAccount());

        return PaymentMapper.mapToPaymentResponse(payment);
    }

    PaymentStatus getPaymentStatus(UUID paymentId) {
        Payment payment = getExistingPayment(paymentId);
        SecurityUtils.performPaymentAuthorization(payment.getSourceAccount(), payment.getTargetAccount());

        return PaymentMapper.mapToPaymentStatus(payment.getStatus());
    }

    private Payment getExistingPayment(UUID paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId);

        if (payment == null) {
            throw new ApiRequestException(PAYMENT_NOT_FOUND_ERROR_MESSAGE, HttpStatus.NOT_FOUND);
        }

        return payment;
    }
}
