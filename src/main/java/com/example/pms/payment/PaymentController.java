package com.example.pms.payment;

import com.example.pms.model.*;
import com.example.pms.utils.ResponseUtils;
import com.example.pms.utils.ValidIban;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<PaymentsResponse> getPayments(
            @ValidIban @RequestParam(required = false) String sourceAccountNumber,
            @ValidIban @RequestParam(required = false) String targetAccountNumber,
            @RequestParam(required = false) PaymentStatusEnum status) {
        PaymentsResponse paymentsResponse = paymentService.getPayments(sourceAccountNumber, targetAccountNumber, status);
        return new ResponseEntity<>(paymentsResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        UUID paymentUUID = paymentService.createPayment(paymentRequest);
        HttpHeaders headers = ResponseUtils.createLocationHeader(String.valueOf(paymentUUID));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId) {
        PaymentResponse paymentResponse = paymentService.getPayment(paymentId);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable UUID paymentId) {
        PaymentStatus paymentStatus = paymentService.getPaymentStatus(paymentId);
        return new ResponseEntity<>(paymentStatus, HttpStatus.OK);
    }
}
