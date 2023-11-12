package com.example.pms.account;

import com.example.pms.model.*;
import com.example.pms.utils.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody @Valid AccountRequest accountRequest) {
        String accountNumber = accountService.createAccount(accountRequest);
        HttpHeaders headers = ResponseUtils.createLocationHeader(accountNumber);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<AccountsResponse> getAccounts() {
        AccountsResponse accountsResponse = accountService.getAccounts();
        return new ResponseEntity<>(accountsResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        AccountResponse targetAccount = accountService.getAccount(accountNumber);
        return new ResponseEntity<>(targetAccount, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{accountNumber}")
    public ResponseEntity<Void> updateAccount(
            @PathVariable String accountNumber,
            @RequestBody @Valid AccountUpdateRequest accountUpdateRequest
    ) {
        accountService.updateAccount(accountNumber, accountUpdateRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{accountNumber}/payments")
    public ResponseEntity<AccountPaymentsResponse> getAccountPayments(@PathVariable String accountNumber) {
        AccountPaymentsResponse accountPaymentsResponse = accountService.getAccountPayments(accountNumber);
        return new ResponseEntity<>(accountPaymentsResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BalanceResponse> getAccountBalance(@PathVariable String accountNumber) {
        BalanceResponse balance = accountService.getAccountBalance(accountNumber);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}

