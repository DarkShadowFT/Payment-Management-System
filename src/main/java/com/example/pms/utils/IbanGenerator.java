package com.example.pms.utils;


import java.util.concurrent.atomic.AtomicLong;

public class IbanGenerator {

    private static final AtomicLong sequence = new AtomicLong(1000);

    public static String generateAccountNumber(int length) {
        long currentSequence = sequence.getAndIncrement();
        return String.format("%0" + length + "d", currentSequence);
    }
}
