package com.example.pms.conversion.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {

    @Query("SELECT cr.exchangeRate FROM ConversionRate cr " +
            "WHERE cr.sourceCurrencyCode = :sourceCurrencyCode AND cr.targetCurrencyCode = :targetCurrencyCode")
    BigDecimal getConversionRate(String sourceCurrencyCode, String targetCurrencyCode);
}
