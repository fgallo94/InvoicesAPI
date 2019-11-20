package com.fgallo94.invoices.exception;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(Long id) {
        super("Could not found invoice: " + id);
    }
}
