package com.example.pms.conversion.rate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class ConversionRateRepositoryTest {

    @Autowired
    private ConversionRateRepository conversionRateRepository;

    @AfterEach
    void tearDown() {
        conversionRateRepository.deleteAll();
    }

    @Test
    void testGetConversionRate() {
        String sourceCurrencyCode = "USD";
        String targetCurrencyCode = "PKR";
        double delta = 0.00001;
        BigDecimal exchangeRate = BigDecimal.valueOf(284.59);

        BigDecimal fetchedExchangeRate = conversionRateRepository.getConversionRate(sourceCurrencyCode, targetCurrencyCode);

        assertEquals(fetchedExchangeRate.doubleValue(), exchangeRate.doubleValue(), delta);
    }

    @Test
    void testNotGetConversionRate() {
        String sourceCurrencyCode = "USD";
        String targetCurrencyCode = "AUD";

        BigDecimal fetchedExchangeRate = conversionRateRepository.getConversionRate(sourceCurrencyCode, targetCurrencyCode);

        assertNull(fetchedExchangeRate);
    }
}