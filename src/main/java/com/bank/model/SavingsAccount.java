package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import java.math.BigDecimal;

public class SavingsAccount extends BankAccount {
    private final BigDecimal minimumBalance;

    public SavingsAccount(String accountNumber, Customer customer, BigDecimal initialBalance, BigDecimal minimumBalance) {
        super(accountNumber, customer, initialBalance);
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Minimum bakiye sÃ„Â±nÃ„Â±rÃ„Â± negatif olamaz.");
        }
        if (initialBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientFundsException("BaÃ…Å¸langÃ„Â±ÃƒÂ§ bakiyesi minimum bakiye sÃ„Â±nÃ„Â±rÃ„Â±ndan dÃƒÂ¼Ã…Å¸ÃƒÂ¼k olamaz: " + minimumBalance);
        }
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        BigDecimal remainingBalance = this.balance.subtract(amount);
        if (remainingBalance.compareTo(this.minimumBalance) < 0) {
            throw new InsufficientFundsException("Tasarruf hesabÃ„Â±nda en az " + minimumBalance + " TL kalmalÃ„Â±dÃ„Â±r.");
        }
        this.balance = remainingBalance;
    }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
}
