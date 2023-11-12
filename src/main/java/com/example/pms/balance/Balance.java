package com.example.pms.balance;

import com.example.pms.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "balances")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", unique = true)
    private Account account;
    private BigDecimal amount;

    @Column(name = "updated_at", updatable = true)
    private LocalDateTime updatedAt;

    @Column(name = "last_updated_payment_id")
    private UUID lastUpdatedPaymentId;

    public Balance(){
        this.updatedAt = LocalDateTime.now();
        this.lastUpdatedPaymentId = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getLastUpdatedPaymentId() {
        return lastUpdatedPaymentId;
    }

    public void setLastUpdatedPaymentId(UUID lastUpdatedPaymentId) {
        this.lastUpdatedPaymentId = lastUpdatedPaymentId;
    }
}
