package com.bank.model;

public class Customer {
    private final String customerId;
    private final String fullName;
    private final String email;

    public Customer(String customerId, String fullName, String email) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("MÃƒÂ¼Ã…Å¸teri ID boÃ…Å¸ olamaz.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("MÃƒÂ¼Ã…Å¸teri adÃ„Â± boÃ…Å¸ olamaz.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("GeÃƒÂ§erli bir e-posta adresi giriniz.");
        }
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}
