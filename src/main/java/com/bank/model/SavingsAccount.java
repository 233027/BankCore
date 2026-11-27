package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import com.bank.strategy.InterestPolicy;
import com.bank.strategy.SavingsAccountInterestPolicy;
import java.math.BigDecimal;

public class SavingsAccount extends BankAccount {
    private final BigDecimal minimumBalance;

    public SavingsAccount(String accountNumber, Customer customer, BigDecimal initialBalance, BigDecimal minimumBalance) {
        this(accountNumber, customer, initialBalance, minimumBalance, new SavingsAccountInterestPolicy());
    }

    public SavingsAccount(String accountNumber, Customer customer, BigDecimal initialBalance, BigDecimal minimumBalance, InterestPolicy interestPolicy) {
        super(accountNumber, customer, initialBalance, interestPolicy);
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Minimum bakiye sınırı negatif olamaz.");
        }
        if (initialBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientFundsException("Başlangıç bakiyesi minimum bakiye sınırından düşük olamaz: " + minimumBalance);
        }
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        BigDecimal remainingBalance = this.balance.subtract(amount);
        if (remainingBalance.compareTo(this.minimumBalance) < 0) {
            throw new InsufficientFundsException("Tasarruf hesabında en az " + minimumBalance + " TL kalmalıdır.");
        }
        this.balance = remainingBalance;
    }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
}