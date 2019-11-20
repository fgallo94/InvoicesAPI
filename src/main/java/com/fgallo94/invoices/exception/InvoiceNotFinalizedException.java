package com.fgallo94.invoices.exception;

public class InvoiceNotFinalizedException extends RuntimeException {
    public InvoiceNotFinalizedException(Long id) {
        super("Invoice not finalized: " + id);
    }
}
