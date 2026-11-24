package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import java.math.BigDecimal;

public class SavingsAccount extends BankAccount {
    private final BigDecimal minimumBalance;

    public SavingsAccount(String accountNumber, Customer customer, BigDecimal initialBalance, BigDecimal minimumBalance) {
        super(accountNumber, customer, initialBalance);
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Minimum bakiye sÄ±nÄ±rÄ± negatif olamaz.");
        }
        if (initialBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientFundsException("BaÅŸlangÄ±Ã§ bakiyesi minimum bakiye sÄ±nÄ±rÄ±ndan dÃ¼ÅŸÃ¼k olamaz: " + minimumBalance);
        }
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        BigDecimal remainingBalance = this.balance.subtract(amount);
        if (remainingBalance.compareTo(this.minimumBalance) < 0) {
            throw new InsufficientFundsException("Tasarruf hesabÄ±nda en az " + minimumBalance + " TL kalmalÄ±dÄ±r.");
        }
        this.balance = remainingBalance;
    }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
}
