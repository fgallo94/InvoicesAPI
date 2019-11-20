package com.fgallo94.invoices.service;

import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.exception.InvoiceNotFinalizedException;
import com.fgallo94.invoices.exception.InvoiceNotFoundException;
import com.fgallo94.invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void delete(Long id) {
        InvoiceResponse invoiceResponse = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
        invoiceRepository.delete(invoiceResponse);
    }

    public InvoiceResponse finalizeInvoice(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .map(invoiceResponse -> {
                    invoiceResponse.getInvoiceStatus()
                            .setFinalizedAt(LocalDateTime.now());
                    invoiceRepository.save(invoiceResponse);
                    return invoiceResponse;
                })
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    public InvoiceResponse payInvoice(Long id) throws InvoiceNotFoundException, InvoiceNotFinalizedException {
        return invoiceRepository.findById(id)
                .map(invoiceResponse -> {
                    if (invoiceResponse.isFinalized()) {
                        invoiceResponse.getInvoiceStatus()
                                .setPaidAt(LocalDateTime.now());
                        invoiceRepository.save(invoiceResponse);
                        return invoiceResponse;
                    } else {
                        throw new InvoiceNotFinalizedException(id);
                    }
                })
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }
}

