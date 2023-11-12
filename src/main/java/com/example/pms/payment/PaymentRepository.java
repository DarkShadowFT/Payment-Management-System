package com.example.pms.payment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p " +
            "WHERE (:sourceAccountNumber is null OR p.sourceAccount.accountNumber = :sourceAccountNumber) " +
            "AND (:targetAccountNumber is null OR p.targetAccount.accountNumber = :targetAccountNumber) " +
            "AND (:paymentStatus is null OR p.status = :paymentStatus)")
    List<Payment> findPaymentsWithFilters(
            @Param("sourceAccountNumber") String sourceAccountNumber,
            @Param("targetAccountNumber") String targetAccountNumber,
            @Param("paymentStatus") String paymentStatus
    );

    Payment findByPaymentId(UUID paymentId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.sourceAccount.accountNumber = :sourceAccountNumber")
    List<Payment> findBySourceAccountNumber(String sourceAccountNumber);
}
