package com.example.pms.conversion.rate;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "conversion_rates")
class ConversionRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_currency_code", length = 3, nullable = false)
    private String sourceCurrencyCode;

    @Column(name = "target_currency_code", length = 3, nullable = false)
    private String targetCurrencyCode;

    @Column(name = "exchange_rate", precision = 10, scale = 5, nullable = false)
    private BigDecimal exchangeRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
