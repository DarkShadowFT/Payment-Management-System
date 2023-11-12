package com.example.pms.account;

import com.example.pms.balance.Balance;
import com.example.pms.balance.BalanceMapper;
import com.example.pms.balance.BalanceRepository;
import com.example.pms.bank.Bank;
import com.example.pms.bank.BankRepository;
import com.example.pms.conversion.rate.ConversionRateRepository;
import com.example.pms.exception.ApiRequestException;
import com.example.pms.model.*;
import com.example.pms.payment.Payment;
import com.example.pms.payment.PaymentRepository;
import com.example.pms.security.authority.Authority;
import com.example.pms.security.authority.AuthorityRepository;
import com.example.pms.security.user.User;
import com.example.pms.security.user.UserRepository;
import com.example.pms.utils.IbanGenerator;
import com.example.pms.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;
import org.iban4j.UnsupportedCountryException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final PaymentRepository paymentRepository;
    private final BalanceRepository balanceRepository;
    private final ConversionRateRepository conversionRateRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account number does not exist!";
    private static final String BANK_NOT_FOUND_ERROR_MESSAGE = "Bank does not exist!";
    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    public AccountService(
            AccountRepository accountRepository,
            BankRepository bankRepository,
            PaymentRepository paymentRepository,
            BalanceRepository balanceRepository,
            ConversionRateRepository conversionRateRepository, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
        this.paymentRepository = paymentRepository;
        this.balanceRepository = balanceRepository;
        this.conversionRateRepository = conversionRateRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    String createAccount(AccountRequest accountRequest) {
        validateBankExists(accountRequest);

        User createdUser = initializeUser();
        Account createdAccount = saveAccount(accountRequest, createdUser);
        saveBalanceWithAccount(createdAccount);

        return createdAccount.getAccountNumber();
    }

    private void validateBankExists(AccountRequest accountRequest){
        boolean bankExists = bankExists(accountRequest.getBankId());
        if (!bankExists){
            throw new ApiRequestException(BANK_NOT_FOUND_ERROR_MESSAGE, HttpStatus.NOT_FOUND);
        }
    }

    private User initializeUser() {
        String username = SecurityUtils.extractUsername();
        User user = userRepository.findByUsername(username);
        List<Authority> authorityList = new ArrayList<>();

        Authority userAuthority = authorityRepository.findByUsername(username);
        userAuthority.setUser(user);
        Authority savedAuthority = authorityRepository.save(userAuthority);
        authorityList.add(savedAuthority);

        user.setAuthorities(authorityList);
        return userRepository.save(user);
    }

    private boolean bankExists(String bankId){
        return bankRepository.existsByBankId(bankId);
    }

    private Account saveAccount(AccountRequest accountRequest, User user){
        Account accountToCreate = AccountMapper.mapToAccount(accountRequest);
        Bank targetBank = bankRepository.findByBankId(accountRequest.getBankId());
        accountToCreate.setBank(targetBank);

        try {
            String accountNumber = generateAccountNumber(targetBank);
            accountToCreate.setAccountNumber(accountNumber);
            accountToCreate.setUser(user);
            return accountRepository.save(accountToCreate);
        }
        catch (UnsupportedCountryException unsupportedCountryException){
            throw new ApiRequestException("Country code is not supported", HttpStatus.BAD_REQUEST);
        }
        catch (IbanFormatException ibanFormatException){
            throw new ApiRequestException(ibanFormatException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String generateAccountNumber(Bank targetBank) {
        int accountNumberLength = targetBank.getBankId().equals("MEZN") ? 16 : 13;

        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.getByCode(targetBank.getCountry()))
                .bankCode(targetBank.getBankId())
                .branchCode("0")
                .accountNumber(IbanGenerator.generateAccountNumber(accountNumberLength))
                .build();

        return iban.toString();
    }

    private void saveBalanceWithAccount(Account account){
        BigDecimal amount = convertAmount(CurrencyEnum.USD.getValue(), account.getCurrencyCode(), BigDecimal.valueOf(3000));

        Balance balance = new Balance();
        balance.setAmount(amount);
        balance.setAccount(account);
        balanceRepository.save(balance);
    }

    private BigDecimal convertAmount(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        if (!sourceCurrency.equals(targetCurrency)) {
            BigDecimal conversionRate = conversionRateRepository.getConversionRate(sourceCurrency, targetCurrency);
            return amount.multiply(conversionRate);
        }
        return amount;
    }

    AccountsResponse getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<Account> filteredAccounts = new ArrayList<>();
        String username = SecurityUtils.extractUsername();
        User user = userRepository.findByUsername(username);

        for (Account account: accounts) {
            if (SecurityUtils.isRole(user, ADMIN_ROLE)) {
                filteredAccounts.add(account);
            }
            else if (SecurityUtils.isRole(user, USER_ROLE) && account.getUser().getUsername().equals(username)) {
                filteredAccounts.add(account);
            }
        }

        return AccountMapper.mapToAccountsResponse(filteredAccounts);
    }

    AccountResponse getAccount(String accountNumber) {
        Account account = getExistingAccount(accountNumber);
        SecurityUtils.performAccountAuthorization(account.getUser().getUsername());

        return AccountMapper.mapToAccountResponse(account);
    }

    @Transactional
    void updateAccount(String accountNumber, AccountUpdateRequest updatedAccountInfo) {
        Account account = getExistingAccount(accountNumber);
        SecurityUtils.performAccountAuthorization(account.getUser().getUsername());

        account.setHolderName(updatedAccountInfo.getHolderName());
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Transactional
    void deleteAccount(String accountNumber) {
        Account account = getExistingAccount(accountNumber);
        SecurityUtils.performAccountAuthorization(account.getUser().getUsername());

        removeReferencedEntities(accountNumber);
        accountRepository.delete(account);
    }

    private void removeReferencedEntities(String accountNumber) {
        Balance balance = balanceRepository.findByAccountNumber(accountNumber);
        balanceRepository.delete(balance);

        List<Payment> paymentList = paymentRepository.findBySourceAccountNumber(accountNumber);
        paymentRepository.deleteAll(paymentList);
    }

    AccountPaymentsResponse getAccountPayments(String accountNumber) {
        Account account = getExistingAccount(accountNumber);
        SecurityUtils.performAccountAuthorization(account.getUser().getUsername());

        List<Payment> paymentHistory = paymentRepository.findBySourceAccountNumber(accountNumber);
        return AccountMapper.mapToAccountPaymentResponse(paymentHistory);
    }

    BalanceResponse getAccountBalance(String accountNumber) {
        Account account = getExistingAccount(accountNumber);
        SecurityUtils.performAccountAuthorization(account.getUser().getUsername());

        Balance targetBalance = balanceRepository.findByAccountNumber(accountNumber);
        return BalanceMapper.mapToBalanceResponse(targetBalance);
    }

    public Account getExistingAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null){
            throw new ApiRequestException(ACCOUNT_NOT_FOUND_ERROR_MESSAGE, HttpStatus.NOT_FOUND);
        }

        return account;
    }
}
