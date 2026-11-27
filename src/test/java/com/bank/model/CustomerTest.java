package com.bank.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void shouldCreateCustomerWhenDataIsValid() {
        Customer musteri1 = new Customer("M-101", "Esma Nur Dinç", "esma@mail.com");
        assertEquals("Esma Nur Dinç", musteri1.getFullName());
    }

    @Test
    void shouldThrowExceptionWhenIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("", "Çağan Demirkıran", "cagan@mail.com");
        });
    }
}