package com.fgallo94.invoices.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class InvoiceStatusTest {

    @Test
    void whenTestPojo_testSetters() {
        InvoiceStatus invoiceStatus = new InvoiceStatus();
        invoiceStatus.setPaidAt(LocalDateTime.now());
        invoiceStatus.setFinalizedAt(LocalDateTime.now());
        invoiceStatus.setInvoiceResponse(new InvoiceResponse());
        invoiceStatus.setInternalCode(1L);
        Assertions.assertNotNull(invoiceStatus.getFinalizedAt());
        Assertions.assertNotNull(invoiceStatus.getInternalCode());
        Assertions.assertNotNull(invoiceStatus.getPaidAt());
        Assertions.assertNotNull(invoiceStatus.getInvoiceResponse());
    }
}
