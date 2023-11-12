package com.example.pms.bank;

import com.example.pms.model.BankInfo;
import com.example.pms.model.BanksResponse;
import com.example.pms.utils.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banks")
@Validated
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<BanksResponse> getBanks() {
        BanksResponse banksResponse = bankService.getBanks();
        return new ResponseEntity<>(banksResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createBank(@RequestBody @Valid BankInfo bankInfo) {
        String bankId = bankService.createBank(bankInfo);
        HttpHeaders headers = ResponseUtils.createLocationHeader(bankId);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
