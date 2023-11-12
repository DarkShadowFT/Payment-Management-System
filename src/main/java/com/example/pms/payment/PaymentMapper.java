package com.example.pms.payment;

import com.example.pms.model.PaymentResponse;
import com.example.pms.model.PaymentStatus;
import com.example.pms.model.PaymentStatusEnum;
import com.example.pms.model.PaymentsResponse;
import com.example.pms.utils.ResponseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentMapper {

    private PaymentMapper() {

    }

    static PaymentResponse mapToPaymentResponse(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(payment.getPaymentId());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setStatus(PaymentStatusEnum.fromValue(payment.getStatus()));
        paymentResponse.setCreatedAt(ResponseUtils.convertToOffsetDateTime(payment.getCreatedAt()));
        paymentResponse.setSourceAccountNumber(payment.getSourceAccountNumber());
        paymentResponse.setTargetAccountNumber(payment.getTargetAccountNumber());
        return paymentResponse;
    }

    static PaymentsResponse mapToPaymentsResponse(List<Payment> paymentList) {
        List<PaymentResponse> paymentResponseList = paymentList.stream()
                .map(PaymentMapper::mapToPaymentResponse)
                .collect(Collectors.toList());

        PaymentsResponse paymentsResponse = new PaymentsResponse();
        paymentsResponse.setPayments(paymentResponseList);
        return paymentsResponse;
    }

    static PaymentStatus mapToPaymentStatus(String status) {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setStatus(PaymentStatusEnum.fromValue(status));
        return paymentStatus;
    }
}
