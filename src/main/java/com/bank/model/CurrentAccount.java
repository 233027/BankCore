package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import java.math.BigDecimal;

public class CurrentAccount extends BankAccount {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(String accountNumber, Customer customer, BigDecimal initialBalance, BigDecimal overdraftLimit) {
        super(accountNumber, customer, initialBalance);
        if (overdraftLimit == null || overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Kredili mevduat limiti negatif olamaz.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        BigDecimal maxWithdrawable = this.balance.add(this.overdraftLimit);
        if (amount.compareTo(maxWithdrawable) > 0) {
            throw new InsufficientFundsException("Vadesiz hesap limiti yetersiz. Ãƒâ€¡ekilebilecek maksimum tutar: " + maxWithdrawable);
        }
        this.balance = this.balance.subtract(amount);
    }

    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
}
