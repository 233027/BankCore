package com.bank.repository;

import com.bank.model.BankAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, BankAccount> storage = new HashMap<>();

    @Override
    public void save(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Kaydedilecek hesap boş olamaz.");
        }
        storage.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(accountNumber));
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return accountNumber != null && storage.containsKey(accountNumber);
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }
}