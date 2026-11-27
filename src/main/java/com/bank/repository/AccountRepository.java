package com.bank.repository;

import com.bank.model.BankAccount;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(BankAccount account);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<BankAccount> findAll();
}