package com.example.pms.bank;

import com.example.pms.exception.ApiRequestException;
import com.example.pms.model.BankInfo;
import com.example.pms.model.BanksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private static final String BANK_EXISTS_ERROR_MESSAGE = "Bank already exists!";
    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    BanksResponse getBanks() {
        List<Bank> bankList = bankRepository.findAll();
        return BankMapper.mapToBanksResponse(bankList);
    }

    String createBank(BankInfo bankInfo) {
        if (bankExists(bankInfo.getBankId())){
            throw new ApiRequestException(BANK_EXISTS_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        Bank createdBank = saveBank(bankInfo);
        return createdBank.getBankId();
    }

    private boolean bankExists(String bankId){
        return bankRepository.existsByBankId(bankId);
    }

    Bank saveBank(BankInfo bankInfo){
        Bank bank = new Bank();
        bank.setName(bankInfo.getName());
        bank.setCountry(bankInfo.getCountry());
        bank.setBankId(bankInfo.getBankId());
        return bankRepository.save(bank);
    }
}
