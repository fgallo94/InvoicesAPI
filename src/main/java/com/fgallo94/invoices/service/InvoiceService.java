package com.fgallo94.invoices.service;

import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.exception.InvoiceNotFoundException;
import com.fgallo94.invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    @Qualifier("invoiceRepository")
    private InvoiceRepository invoiceRepository;

    public void insert(InvoiceResponse invoiceResponse) {
        invoiceRepository.save(invoiceResponse);
    }

    public InvoiceResponse getById(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public InvoiceResponse updateInvoice(Long id, InvoiceResponse invoiceResponseNew) {
        return invoiceRepository.findById(id)
                .map(invoiceResponseActual -> {
                    invoiceResponseActual.setCharacter(invoiceResponseNew.getCharacter());
                    invoiceResponseActual.setCustomerMail(invoiceResponseNew.getCustomerMail());
                    invoiceResponseActual.setDateTime(invoiceResponseNew.getDateTime());
                    invoiceResponseActual.setDiscount(invoiceResponseNew.getDiscount());
                    invoiceResponseActual.setInvoiceStatus(invoiceResponseNew.getInvoiceStatus());
                    invoiceResponseActual.setLines(invoiceResponseNew.getLines());
                    invoiceResponseActual.setNumber(invoiceResponseNew.getNumber());
                    invoiceResponseActual.setPayMethod(invoiceResponseNew.getPayMethod());
                    invoiceResponseActual.setRecharge(invoiceResponseNew.getRecharge());
                    return invoiceRepository.save(invoiceResponseNew);
                })
                .orElseGet(() -> {
                    invoiceResponseNew.setInternalCode(id);
                    return invoiceRepository.save(invoiceResponseNew);
                });
    }
}

