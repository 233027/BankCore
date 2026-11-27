package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final String description;

    public Transaction(TransactionType type, BigDecimal amount, LocalDate date,
                       String sourceAccountNumber, String targetAccountNumber, String description) {
        if (type == null) {
            throw new IllegalArgumentException("İşlem türü boş olamaz.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("İşlem tutarı negatif olamaz.");
        }
        if (date == null) {
            throw new IllegalArgumentException("İşlem tarihi boş olamaz.");
        }
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public String getDescription() { return description; }
}