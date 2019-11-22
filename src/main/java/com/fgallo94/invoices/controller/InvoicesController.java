package com.fgallo94.invoices.controller;

import com.fgallo94.invoices.entity.Invoice;
import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.entity.Lines;
import com.fgallo94.invoices.exception.InvoiceNotFinalizedException;
import com.fgallo94.invoices.exception.InvoiceNotFoundException;
import com.fgallo94.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("invoices")
public class InvoicesController {

    @Autowired
    @Qualifier("invoiceService")
    private InvoiceService invoiceService;

    @PostMapping("/")
    public ResponseEntity<InvoiceResponse> insertInvoice(@RequestBody Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        invoiceService.insert(invoiceResponse);
        return new ResponseEntity<>(invoiceResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id, @RequestBody InvoiceResponse invoice) {
        invoice = invoiceService.updateInvoice(id, invoice);
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoicesResponse = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoicesResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long id) {
        try {
            InvoiceResponse invoiceResponse = invoiceService.getById(id);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.FOUND);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InvoiceResponse> deleteInvoice(@PathVariable Long id) {
        try {
            invoiceService.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<InvoiceResponse> finalizeInvoice(@PathVariable Long id) {
        try {
            InvoiceResponse invoice = invoiceService.finalizeInvoice(id);
            return new ResponseEntity<>(invoice, HttpStatus.CREATED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceResponse> payInvoice(@PathVariable Long id) {
        try {
            InvoiceResponse invoice = invoiceService.payInvoice(id);
            return new ResponseEntity<>(invoice, HttpStatus.CREATED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InvoiceNotFinalizedException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/line")
    public ResponseEntity<InvoiceResponse> addLine(@PathVariable Long id, @RequestBody Lines line) {
        try {
            InvoiceResponse invoiceResponse = invoiceService.addLine(id, line);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.ACCEPTED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{idInvoice}/line/{idLine}")
    public ResponseEntity<InvoiceResponse> deleteLine(@PathVariable Long idInvoice, @PathVariable Long idLine){
        try {
            InvoiceResponse invoiceResponse = invoiceService.deleteLine(idInvoice, idLine);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.ACCEPTED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{idInvoice}/line")
    public ResponseEntity<InvoiceResponse> deleteLine(@PathVariable Long idInvoice){
        try {
            InvoiceResponse invoiceResponse = invoiceService.deleteAllLines(idInvoice);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.ACCEPTED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/{idInvoice}/line/{idLine}")
    public ResponseEntity<InvoiceResponse> updateLine(@PathVariable Long idInvoice, @PathVariable Long idLine, @RequestBody Lines newLine){
        try {
            InvoiceResponse invoiceResponse = invoiceService.updateLine(idInvoice, idLine, newLine);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.ACCEPTED);
        } catch (InvoiceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
