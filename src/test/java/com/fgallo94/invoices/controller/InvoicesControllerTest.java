package com.fgallo94.invoices.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fgallo94.invoices.entity.Invoice;
import com.fgallo94.invoices.entity.InvoiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InvoicesControllerTest {


    // POST /invoices/{id}
    // DELETE /invoices/{id}
    // GET /invoices/
    // POST /invoices/{id}/finalize
    // POST /invoices/{id}/pay
    @Autowired
    private WebApplicationContext wac;

    private InvoicesController invoicesController;

    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoicesController = new InvoicesController();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        mapper = new ObjectMapper();
        invoice = Invoice.builder()
                .number(1l)
                .character('A')
                .customerMail("false@mail.com")
                .build();
        InvoiceResponse invoiceResponseMock = new InvoiceResponse(invoice);
    }

    // POST /invoices/
    @Test
    public void whenSendInvoices_withCorrectParameters_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponseExpected = new InvoiceResponse(invoice);
        MvcResult result = mockMvc.perform(post("/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        InvoiceResponse invoiceResponseActual = mapper.readValue(content, InvoiceResponse.class);
        Assertions.assertEquals(invoiceResponseExpected, invoiceResponseActual);
    }

    // GET /invoices/{id}
    @Test
    public void whenGetInvoices_withId_thenReturnInvoice() throws Exception {
        MvcResult result = mockMvc.perform(get("/invoices/1"))
                .andExpect(status().isFound())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        InvoiceResponse invoiceResponse = mapper.readValue(content, InvoiceResponse.class);
        Assertions.assertTrue(Objects.nonNull(invoiceResponse));
    }
}
