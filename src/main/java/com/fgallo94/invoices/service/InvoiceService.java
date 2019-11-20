package com.fgallo94.invoices.service;

import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    @Qualifier("invoiceRepository")
    private InvoiceRepository invoiceRepository;

    public void insert(InvoiceResponse invoiceResponse) {
        invoiceRepository.save(invoiceResponse);
    }

    public InvoiceResponse getById(Long id) {
        return invoiceRepository.findById(id).get();
    }
}
