package com.bank.strategy;

import java.math.BigDecimal;

public interface InterestPolicy {
    BigDecimal calculateMonthlyInterest(BigDecimal balance);
}