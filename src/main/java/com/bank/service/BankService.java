package com.bank.service;

import com.bank.exception.AccountFrozenException;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.DuplicateAccountException;
import com.bank.exception.InvalidAmountException;
import com.bank.exception.InvalidTransferException;
import com.bank.factory.AccountFactory;
import com.bank.model.AccountStatus;
import com.bank.model.AccountType;
import com.bank.model.BankAccount;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankService {
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final List<Transaction> transactions;

    public BankService(AccountRepository accountRepository, AccountFactory accountFactory) {
        if (accountRepository == null || accountFactory == null) {
            throw new IllegalArgumentException("Repository ve Factory boş olamaz.");
        }
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.transactions = new ArrayList<>();
    }

    public BankAccount openAccount(AccountType type, String accountNumber, Customer customer, BigDecimal initialBalance) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new DuplicateAccountException("Bu hesap numarası zaten mevcut: " + accountNumber);
        }
        BankAccount account = accountFactory.createAccount(type, accountNumber, customer, initialBalance);
        accountRepository.save(account);
        return account;
    }

    public void deposit(String accountNumber, BigDecimal amount, LocalDate date) {
        validateDate(date);
        BankAccount account = getAccountOrThrow(accountNumber);
        account.deposit(amount);
        transactions.add(new Transaction(
                TransactionType.DEPOSIT,
                amount,
                date,
                null,
                accountNumber,
                "Para yatırma işlemi"
        ));
    }

    public void withdraw(String accountNumber, BigDecimal amount, LocalDate date) {
        validateDate(date);
        BankAccount account = getAccountOrThrow(accountNumber);
        account.withdraw(amount);
        transactions.add(new Transaction(
                TransactionType.WITHDRAW,
                amount,
                date,
                accountNumber,
                null,
                "Para çekme işlemi"
        ));
    }

    public void transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, LocalDate date) {
        validateDate(date);
        if (sourceAccountNumber == null || targetAccountNumber == null || sourceAccountNumber.equals(targetAccountNumber)) {
            throw new InvalidTransferException("Kaynak ve hedef hesap numaraları farklı ve geçerli olmalıdır.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Transfer tutarı sıfırdan büyük olmalıdır.");
        }

        BankAccount source = getAccountOrThrow(sourceAccountNumber);
        BankAccount target = getAccountOrThrow(targetAccountNumber);

        if (source.getStatus() == AccountStatus.FROZEN || target.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Dondurulmuş hesaplar arasında transfer yapılamaz.");
        }

        source.withdraw(amount);
        target.deposit(amount);

        transactions.add(new Transaction(
                TransactionType.TRANSFER,
                amount,
                date,
                sourceAccountNumber,
                targetAccountNumber,
                "Hesaplar arası transfer"
        ));
    }

    public BigDecimal applyMonthlyInterest(String accountNumber, LocalDate date) {
        validateDate(date);
        BankAccount account = getAccountOrThrow(accountNumber);
        BigDecimal interestAmount = account.applyMonthlyInterest(date);
        transactions.add(new Transaction(
                TransactionType.INTEREST,
                interestAmount,
                date,
                null,
                accountNumber,
                "Aylık faiz ödemesi"
        ));
        return interestAmount;
    }

    public void freezeAccount(String accountNumber) {
        BankAccount account = getAccountOrThrow(accountNumber);
        account.freeze();
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        getAccountOrThrow(accountNumber);
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (accountNumber.equals(tx.getSourceAccountNumber()) || accountNumber.equals(tx.getTargetAccountNumber())) {
                result.add(tx);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private BankAccount getAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı: " + accountNumber));
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("İşlem tarihi boş olamaz.");
        }
    }
}