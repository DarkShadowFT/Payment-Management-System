package com.example.pms.bank;

import com.example.pms.model.BankInfo;
import com.example.pms.model.BanksResponse;

import java.util.ArrayList;
import java.util.List;

public class BankMapper {

    private BankMapper() {

    }

    static BankInfo mapToBankInfo(Bank bank) {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankId(bank.getBankId());
        bankInfo.setCountry(bank.getCountry());
        bankInfo.setName(bank.getName());
        return bankInfo;
    }

    static BanksResponse mapToBanksResponse(List<Bank> bankList){
        List<BankInfo> bankInfos = new ArrayList<>();

        for(Bank bank: bankList){
            BankInfo bankResponse = mapToBankInfo(bank);
            bankInfos.add(bankResponse);
        }

        BanksResponse banksResponse = new BanksResponse();
        banksResponse.setBanks(bankInfos);
        return banksResponse;
    }
}
