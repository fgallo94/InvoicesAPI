package com.fgallo94.invoices.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fgallo94.invoices.entity.Invoice;
import com.fgallo94.invoices.entity.InvoiceResponse;
import com.fgallo94.invoices.entity.Lines;
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

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    private Invoice invoice;

    private Lines newLine;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        mapper.registerModule(new JavaTimeModule());

        invoice = Invoice.builder()
                .number(1L)
                .character('A')
                .customerMail("false@mail.com")
                .build();
        newLine = Lines.builder()
                .item("Men Cut")
                .description("Hair Cut")
                .quantity(1L)
                .price(250D)
                .build();
    }

    @Test
    void whenSendInvoices_withCorrectParameters_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        Assertions.assertNotNull(invoiceResponse);
    }

    @Test
    void whenGetInvoices_withId_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        MvcResult result = mockMvc.perform(get("/invoices/" + invoiceResponse.getInternalCode()))
                .andExpect(status().isFound())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        invoiceResponse = mapper.readValue(content, InvoiceResponse.class);
        Assertions.assertTrue(Objects.nonNull(invoiceResponse));
    }

    @Test
    void whenGetInvoices_withFailedId_thenReturnInvoice() throws Exception {
        mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(get("/invoices/23435"))
                .andExpect(status().isNoContent());
    }

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

    @Test
    void whenPostUpdateInvoices_withModifiedInvoice_thenReturnModifiedInvoice() throws Exception {
        InvoiceResponse invoiceResponseOld = mockServicePostAndReturnInvoiceResponse("/invoices/");
        invoice.setCharacter('B');
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        mockMvc.perform(post("/invoices/" + invoiceResponseOld.getInternalCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceResponse)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.character", is("B")));
    }

    @Test
    void whenPostUpdateInvoices_withNewInvoice_thenReturnSuccessful() throws Exception {
        invoice.setCharacter('B');
        InvoiceResponse invoiceResponse = new InvoiceResponse(invoice);
        mockMvc.perform(post("/invoices/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceResponse)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.character", is("B")));
    }

    @Test
    void whenDeleteInvoice_withId_thenReturnAcepted() throws Exception {
        mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(delete("/invoices/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void whenDeleteInvoice_withFailedId_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/invoices/12512521124"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFinalizeAnInvoice_withCorrectId_thenReturnSuccessful() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/finalize"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenFinalizeAnInvoice_withFailedId_thenReturnNoContent() throws Exception {
        mockMvc.perform(post("/invoices/12312412/finalize"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPayAnInvoice_withCorrectInfo_thenReturnCreated() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/finalize"))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/pay"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenPayAnInvoice_withPartialInfo_thenReturnClientError() throws Exception {
        mockMvc.perform(post("/invoices/2/pay"))
                .andExpect(status().is4xxClientError());
    }

    // POST /invoices/{id}/pay ID NOT SAVED
    @Test
    void whenPayAnInvoice_withFailedId_thenReturnNoContent() throws Exception {
        mockMvc.perform(post("/invoices/1231411234/pay"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenAddLineToInvoice_withCorrectId_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDeleteLineToInvoice_withCorrectId_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(delete("/invoices/" + invoiceResponse.getInternalCode() + "/line/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDeleteAllLines_withInvoiceId_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
        newLine.setItem("Women Cut");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(delete("/invoices/" + invoiceResponse.getInternalCode() + "/line/"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdateASpecificLineInInvoice_withInvoiceIdAndLineId_thenReturnInvoice() throws Exception {
        InvoiceResponse invoiceResponse = mockServicePostAndReturnInvoiceResponse("/invoices/");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
        newLine.setItem("Women Cut");
        mockMvc.perform(post("/invoices/" + invoiceResponse.getInternalCode() + "/line/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdateLines_withFailInvoiceId_thenReturnInvoice() throws Exception {
        mockMvc.perform(post("/invoices/" + 1231292 + "/line/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteLines_withFailInvoiceId_thenReturnInvoice() throws Exception {
        mockMvc.perform(delete("/invoices/" + 1231292 + "/line/"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenAddLine_withFailedInvoiceId_thenReturnNoContent() throws Exception {
        mockMvc.perform(post("/invoices/12312412/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newLine.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteLine_withFailedInvoiceId_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/invoices/1234123/line/1"))
                .andExpect(status().isNoContent());
    }

    private InvoiceResponse mockServicePostAndReturnInvoiceResponse(String url) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice)))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse()
                .getContentAsString();
        return mapper.readValue(content, InvoiceResponse.class);
    }
}
