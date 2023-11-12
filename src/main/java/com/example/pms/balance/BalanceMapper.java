package com.example.pms.balance;

import com.example.pms.model.BalanceResponse;
import com.example.pms.utils.ResponseUtils;

public class BalanceMapper {
    private BalanceMapper() {

    }

    public static BalanceResponse mapToBalanceResponse(Balance balance){
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setAmount(balance.getAmount());
        balanceResponse.setUpdatedAt(ResponseUtils.convertToOffsetDateTime(balance.getUpdatedAt()));
        balanceResponse.setLastUpdatedPaymentId(balance.getLastUpdatedPaymentId());
        return balanceResponse;
    }
}
