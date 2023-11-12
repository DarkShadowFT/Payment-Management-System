package com.example.pms.account;

import com.example.pms.model.*;
import com.example.pms.payment.Payment;
import com.example.pms.utils.ResponseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AccountMapper {

    private AccountMapper() {

    }
    static Account mapToAccount(AccountRequest accountRequest) {
        Account account = new Account();
        account.setCurrencyCode(accountRequest.getCurrency().getValue().toUpperCase(Locale.ROOT));
        account.setHolderName(accountRequest.getHolderName());
        return account;
    }

    static AccountsResponse mapToAccountsResponse(List<Account> accountList) {
        List<AccountResponse> accountResponseList = accountList.stream()
                .map(AccountMapper::mapToAccountResponse)
                .collect(Collectors.toList());

        AccountsResponse accountsResponse = new AccountsResponse();
        accountsResponse.setAccounts(accountResponseList);
        return accountsResponse;
    }

    static AccountResponse mapToAccountResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setHolderName(account.getHolderName());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setBankId(account.getBankId());
        accountResponse.setCurrency(CurrencyEnum.fromValue(account.getCurrencyCode()));
        return accountResponse;
    }

    static AccountPayment mapToAccountPayment(Payment payment) {
        AccountPayment accountPayment = new AccountPayment();
        accountPayment.setPaymentId(payment.getPaymentId());
        accountPayment.setTargetAccountNumber(payment.getTargetAccountNumber());
        accountPayment.setAmount(payment.getAmount());
        accountPayment.setCreatedAt(ResponseUtils.convertToOffsetDateTime(payment.getCreatedAt()));
        accountPayment.setStatus(PaymentStatusEnum.fromValue(payment.getStatus()));
        return accountPayment;
    }

    static AccountPaymentsResponse mapToAccountPaymentResponse(List<Payment> paymentList){
        List<AccountPayment> accountPaymentList = new ArrayList<>();
        for (Payment payment: paymentList){
            AccountPayment accountPayment = AccountMapper.mapToAccountPayment(payment);
            accountPaymentList.add(accountPayment);
        }

        AccountPaymentsResponse accountPaymentsResponse = new AccountPaymentsResponse();
        accountPaymentsResponse.setAccountPayments(accountPaymentList);
        return accountPaymentsResponse;
    }
}
