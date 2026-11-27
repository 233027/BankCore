package com.bank.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SavingsAccountInterestPolicy implements InterestPolicy {
    private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.035");

    @Override
    public BigDecimal calculateMonthlyInterest(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return balance.multiply(MONTHLY_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}