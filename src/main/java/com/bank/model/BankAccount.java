package com.bank.model;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.InvalidInterestDateException;
import com.bank.strategy.InterestPolicy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public abstract class BankAccount {
    private final String accountNumber;
    private final Customer customer;
    protected BigDecimal balance;
    private AccountStatus status;
    private InterestPolicy interestPolicy;
    private YearMonth lastInterestMonth;

    protected BankAccount(String accountNumber, Customer customer, BigDecimal initialBalance, InterestPolicy interestPolicy) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Hesap numarası boş olamaz.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Müşteri bilgisi boş olamaz.");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Başlangıç bakiyesi negatif olamaz.");
        }
        if (interestPolicy == null) {
            throw new IllegalArgumentException("Faiz politikası boş olamaz.");
        }
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.interestPolicy = interestPolicy;
    }

    public void deposit(BigDecimal amount) {
        validateActiveAndPositiveAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public abstract void withdraw(BigDecimal amount);

    public BigDecimal applyMonthlyInterest(LocalDate date) {
        if (this.status == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Dondurulmuş hesaba faiz uygulanamaz: " + accountNumber);
        }
        if (date == null) {
            throw new InvalidInterestDateException("Faiz tarihi boş olamaz.");
        }
        YearMonth targetMonth = YearMonth.from(date);
        if (this.lastInterestMonth != null && !targetMonth.isAfter(this.lastInterestMonth)) {
            throw new InvalidInterestDateException("Bu ay veya önceki bir ay için zaten faiz uygulanmış: " + targetMonth);
        }
        BigDecimal interestAmount = interestPolicy.calculateMonthlyInterest(this.balance);
        this.balance = this.balance.add(interestAmount);
        this.lastInterestMonth = targetMonth;
        return interestAmount;
    }

    public void freeze() {
        this.status = AccountStatus.FROZEN;
    }

    protected void validateActiveAndPositiveAmount(BigDecimal amount) {
        if (this.status == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Hesap dondurulmuş durumda, işlem yapılamaz: " + accountNumber);
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("İşlem tutarı sıfırdan büyük olmalıdır.");
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public Customer getCustomer() { return customer; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public InterestPolicy getInterestPolicy() { return interestPolicy; }
    public void setInterestPolicy(InterestPolicy interestPolicy) { this.interestPolicy = interestPolicy; }
    public YearMonth getLastInterestMonth() { return lastInterestMonth; }
}