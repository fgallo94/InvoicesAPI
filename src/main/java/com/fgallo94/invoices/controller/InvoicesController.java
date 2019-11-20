package com.fgallo94.invoices.controller;

import com.fgallo94.invoices.entity.Invoice;
import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("invoices")
public class InvoicesController {

    @Autowired
    @Qualifier("invoiceService")
    private InvoiceService invoiceService;

    @PostMapping("/")
    public ResponseEntity<InvoiceResponse> insertInvoice(@RequestBody Invoice invoice){
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        invoiceService.insert(invoiceResponse);
        return new ResponseEntity<InvoiceResponse>(invoiceResponse ,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long id){
        InvoiceResponse invoiceResponse = invoiceService.getById(id);
        return new ResponseEntity<InvoiceResponse>(invoiceResponse, HttpStatus.FOUND);
    }
}
