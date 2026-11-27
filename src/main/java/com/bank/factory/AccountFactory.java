package com.bank.factory;

import com.bank.model.AccountType;
import com.bank.model.BankAccount;
import com.bank.model.CurrentAccount;
import com.bank.model.Customer;
import com.bank.model.SavingsAccount;
import com.bank.strategy.CurrentAccountInterestPolicy;
import com.bank.strategy.SavingsAccountInterestPolicy;
import java.math.BigDecimal;

public class AccountFactory {
    public static final BigDecimal DEFAULT_OVERDRAFT_LIMIT = new BigDecimal("1000.00");
    public static final BigDecimal DEFAULT_MINIMUM_BALANCE = new BigDecimal("500.00");

    public BankAccount createAccount(AccountType type, String accountNumber, Customer customer, BigDecimal initialBalance) {
        if (type == null) {
            throw new IllegalArgumentException("Hesap türü boş olamaz.");
        }
        return switch (type) {
            case CURRENT -> new CurrentAccount(
                    accountNumber,
                    customer,
                    initialBalance,
                    DEFAULT_OVERDRAFT_LIMIT,
                    new CurrentAccountInterestPolicy()
            );
            case SAVINGS -> new SavingsAccount(
                    accountNumber,
                    customer,
                    initialBalance,
                    DEFAULT_MINIMUM_BALANCE,
                    new SavingsAccountInterestPolicy()
            );
        };
    }
}