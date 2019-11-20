package com.fgallo94.invoices.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fgallo94.invoices.entity.Invoice;
import com.fgallo94.invoices.entity.InvoiceResponse;
import org.junit.jupiter.api.AfterEach;
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

import java.util.ArrayList;
import java.util.Objects;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class InvoicesControllerTest {

    // POST /invoices/{id}/pay
    @Autowired
    private WebApplicationContext wac;


    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private Invoice invoice;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        mapper = new ObjectMapper();
        invoice = Invoice.builder()
                .number(1L)
                .character('A')
                .customerMail("false@mail.com")
                .build();
    }

    // POST /invoices/
    @Test
    void whenSendInvoices_withCorrectParameters_thenReturnInvoice() throws Exception {
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
    void whenGetInvoices_withId_thenReturnInvoice() throws Exception {
        mockMvc.perform(post("/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated());
        MvcResult result = mockMvc.perform(get("/invoices/3"))
                .andExpect(status().isFound())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        InvoiceResponse invoiceResponse = mapper.readValue(content, InvoiceResponse.class);
        Assertions.assertTrue(Objects.nonNull(invoiceResponse));
    }

    // GET /invoices/{id} Exception
    @Test
    void whenGetInvoices_withFailedId_thenReturnInvoice() throws Exception {
        mockMvc.perform(post("/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/invoices/23435"))
                .andExpect(status().isNoContent());
    }

    // GET /invoices/
    @Test
    void whenGetAllInvoices_withoutParameters_thenReturnAllInvoices() throws Exception {
        ArrayList<InvoiceResponse> invoiceResponseArrayList = new ArrayList<InvoiceResponse>();
        MvcResult result = mockMvc.perform(get("/invoices/"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        InvoiceResponse[] responses = mapper.readValue(content, InvoiceResponse[].class);
        for (InvoiceResponse invoice : responses) {
            Assertions.assertTrue(Objects.nonNull(invoice));
        }
    }

    // POST /invoices/{id}
    @Test
    void whenPostUpdateInvoices_withModifiedInvoice_thenReturnAllInvoices() throws Exception {
        mockMvc.perform(post("/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated());
        invoice.setCharacter('B');
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        mockMvc.perform(post("/invoices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceResponse)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.character", is("B")));
    }

    // POST /invoices/{id}
    @Test
    void whenPostUpdateInvoices_withNewInvoice_thenReturnAllInvoices() throws Exception {
        invoice.setCharacter('B');
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        mockMvc.perform(post("/invoices/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceResponse)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.character", is("B")));
    }

    // DELETE /invoices/{id}
    @Test
    void whenDeleteInvoice_withId_thenReturnCorrectStatus() throws Exception {
        mockMvc.perform(post("/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/invoices/1"))
                .andExpect(status().isAccepted());
    }

    // DELETE /invoices/{id}
    @Test
    void whenDeleteInvoice_withFailedId_thenReturnCorrectStatus() throws Exception {
        mockMvc.perform(delete("/invoices/12512521124"))
                .andExpect(status().isNoContent());
    }

    // POST /invoices/{id}/finalize
    @Test
    void whenFinalizeAnInvoice_withCorrectId_thenReturnInvoice() throws Exception {
        mockMvc.perform(post("/invoices/2/finalize"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.invoiceStatus.finalizedAt").isNotEmpty());
    }

    // POST /invoices/{id}/finalize
    @Test
    void whenFinalizeAnInvoice_withFailedId_thenReturnInvoice() throws Exception {
        mockMvc.perform(post("/invoices/12312412/finalize"))
                .andExpect(status().isNoContent());
    }
}
