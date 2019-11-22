package com.fgallo94.invoices.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinesTest {
    @Test
    void whenTestInvoiceResponse() {
        Lines lines = new Lines();
        lines.setInvoiceResponse(new InvoiceResponse());
        Assertions.assertNotNull(lines.getInvoiceResponse());
    }
}
