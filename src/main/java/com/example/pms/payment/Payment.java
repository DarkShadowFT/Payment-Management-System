package com.example.pms.payment;

import com.example.pms.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private UUID paymentId;

    @ManyToOne
    @JoinColumn(name = "source_account_id", referencedColumnName = "id")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", referencedColumnName = "id")
    private Account targetAccount;

    private BigDecimal amount;

    @Column(name = "status_code")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Payment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.paymentId = UUID.randomUUID();
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSourceAccountNumber() {
        return sourceAccount.getAccountNumber();
    }

    public String getTargetAccountNumber() {
        return targetAccount.getAccountNumber();
    }
}
