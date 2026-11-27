package com.bank.factory;

import com.bank.model.AccountType;
import com.bank.model.BankAccount;
import com.bank.model.CurrentAccount;
import com.bank.model.Customer;
import com.bank.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountFactoryTest {
    private AccountFactory factory;
    private Customer customer;

    @BeforeEach
    void setUp() {
        factory = new AccountFactory();
        customer = new Customer("CUST-01", "Esma Nur Dinç", "esma@bank.com");
    }

    @Test
    void shouldCreateCurrentAccountWithDefaultOverdraftAndInterestPolicy() {
        BankAccount account = factory.createAccount(AccountType.CURRENT, "TR10", customer, new BigDecimal("1000.00"));

        assertInstanceOf(CurrentAccount.class, account);
        BigDecimal interest = account.applyMonthlyInterest(LocalDate.of(2026, 11, 30));
        assertEquals(new BigDecimal("5.00"), interest);
    }

    @Test
    void shouldCreateSavingsAccountWithHigherMonthlyInterestPolicy() {
        BankAccount account = factory.createAccount(AccountType.SAVINGS, "TR20", customer, new BigDecimal("1000.00"));

        assertInstanceOf(SavingsAccount.class, account);
        BigDecimal interest = account.applyMonthlyInterest(LocalDate.of(2026, 11, 30));
        assertEquals(new BigDecimal("35.00"), interest);
    }

    @Test
    void shouldThrowExceptionWhenAccountTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createAccount(null, "TR30", customer, new BigDecimal("1000.00"));
        });
    }
}