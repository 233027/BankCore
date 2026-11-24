package com.bank.model;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.time.YearMonth;

public abstract class BankAccount {
    private final String accountNumber;
    private final Customer customer;
    protected BigDecimal balance;
    private AccountStatus status;
    private YearMonth lastInterestMonth;

    protected BankAccount(String accountNumber, Customer customer, BigDecimal initialBalance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Hesap numarasÄ± boÅŸ olamaz.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("MÃ¼ÅŸteri bilgisi boÅŸ olamaz.");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("BaÅŸlangÄ±Ã§ bakiyesi negatif olamaz.");
        }
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
    }

    public void deposit(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public abstract void withdraw(BigDecimal amount);

    public void freeze() {
        this.status = AccountStatus.FROZEN;
    }

    protected void validateActiveAndPositiveAmount(BigDecimal amount) {
        if (this.status == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Hesap dondurulmuÅŸ durumda, iÅŸlem yapÄ±lamaz: " + accountNumber);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Ä°ÅŸlem tutarÄ± sÄ±fÄ±rdan bÃ¼yÃ¼k olmalÄ±dÄ±r.");
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public Customer getCustomer() { return customer; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public YearMonth getLastInterestMonth() { return lastInterestMonth; }
    public void setLastInterestMonth(YearMonth lastInterestMonth) { this.lastInterestMonth = lastInterestMonth; }
}
