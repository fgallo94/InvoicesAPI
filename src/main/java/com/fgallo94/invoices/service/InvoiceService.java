package com.fgallo94.invoices.service;

import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.entity.Lines;
import com.fgallo94.invoices.exception.InvoiceNotFinalizedException;
import com.fgallo94.invoices.exception.InvoiceNotFoundException;
import com.fgallo94.invoices.repository.InvoiceRepository;
import com.fgallo94.invoices.repository.LinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
/**
 * Main service of the project
 */
public class InvoiceService {

    @Autowired
    @Qualifier("invoiceRepository")
    private InvoiceRepository invoiceRepository;

    @Autowired
    private LinesRepository linesRepository;

    /**
     * Insert an InvoiceResponse into Database
     *
     * @param invoiceResponse Invoice to be inserted
     */
    public void insert(InvoiceResponse invoiceResponse) {
        invoiceResponse.setDateTime(LocalDateTime.now());
        invoiceRepository.save(invoiceResponse);
    }

    /**
     * Get an invoice of the database by id
     *
     * @param id id to be searched
     * @return searched invoice
     * @throws InvoiceNotFoundException throws if fail the search
     */
    public InvoiceResponse getById(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    /**
     * Get all the invoices of the database
     *
     * @return List<InvoiceResponse>
     */
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    /**
     * Update an indicated Invoice, if not exist save a new one
     *
     * @param id                 id to be searched
     * @param invoiceResponseNew New invoice to update
     * @return new Invoice
     */
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
                    invoiceResponseNew.setDateTime(LocalDateTime.now());
                    return invoiceRepository.save(invoiceResponseNew);
                });
    }

    /**
     * Delete by id
     *
     * @param id to be deleted
     */
    public void delete(Long id) {
        InvoiceResponse invoiceResponse = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
        invoiceRepository.delete(invoiceResponse);
    }

    /**
     * Get an invoice by id then finalize with the actual date time and save it.
     *
     * @param id invoice to be finalized
     * @return invoice modified
     * @throws InvoiceNotFoundException throws if fail the serach
     */
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

    /**
     * Get an invoice by id then if is finalized complete the pay status with the actual time.
     *
     * @param id invoice to be paid.
     * @return modified invoice
     * @throws InvoiceNotFoundException     throws if fail the search.
     * @throws InvoiceNotFinalizedException throws if is not finalized.
     */
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

    /**
     * Search an invoice by id and add a new line that invoice and save it.
     *
     * @param id   invoice to be modified
     * @param line to be inserted
     * @return modified invoice
     * @throws InvoiceNotFoundException throws if fail the search
     */
    public InvoiceResponse addLine(Long id, Lines line) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .map(invoiceResponse -> {
                    line.setInvoiceResponse(invoiceResponse);
                    invoiceResponse.getLines()
                            .add(line);
                    invoiceRepository.save(invoiceResponse);
                    return invoiceResponse;
                })
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }

    /**
     * Get an invoice by id then remove an specific line by id and save it.
     *
     * @param idInvoice invoice to be modified
     * @param idLine    line to be removed
     * @return modified invoice
     * @throws InvoiceNotFoundException throws if fail the search.
     */
    public InvoiceResponse deleteLine(Long idInvoice, Long idLine) throws InvoiceNotFoundException {
        return invoiceRepository.findById(idInvoice)
                .map(invoiceResponse -> {
                    invoiceResponse.getLines()
                            .stream()
                            .filter(lines -> lines.getInternalCodes() == idLine)
                            .forEach(lines -> linesRepository.delete(lines));
                    invoiceRepository.save(invoiceResponse);
                    return invoiceResponse;
                })
                .orElseThrow(() -> new InvoiceNotFoundException(idInvoice));
    }

    /**
     * Get an invoice by id and delete all the line of that invoice.
     *
     * @param idInvoice invoice to be modified
     * @return invoice modified
     * @throws InvoiceNotFoundException throws if fail the search
     */
    public InvoiceResponse deleteAllLines(Long idInvoice) throws InvoiceNotFoundException {
        return invoiceRepository.findById(idInvoice)
                .map(invoiceResponse -> {
                    invoiceResponse.getLines()
                            .stream()
                            .forEach(lines -> linesRepository.delete(lines));
                    invoiceRepository.save(invoiceResponse);
                    return invoiceResponse;
                })
                .orElseThrow(() -> new InvoiceNotFoundException(idInvoice));
    }

    /**
     * Get an invoice by id then filter the lines until get the searched one and modified to save it.
     *
     * @param idInvoice invoice to be modified
     * @param idLine    line to be modified
     * @param newLine   new line to be inserted
     * @return modified invoice
     * @throws InvoiceNotFoundException throws if fails the search.
     */
    public InvoiceResponse updateLine(Long idInvoice, Long idLine, Lines newLine) throws InvoiceNotFoundException {
        return invoiceRepository.findById(idInvoice)
                .map(invoiceResponse -> {
                    invoiceResponse.getLines()
                            .stream()
                            .filter(lines -> lines.getInternalCodes() == idLine)
                            .forEach(lines -> linesRepository.save(newLine));
                    invoiceRepository.save(invoiceResponse);
                    return invoiceResponse;
                })
                .orElseThrow(() -> new InvoiceNotFoundException(idInvoice));
    }
}

