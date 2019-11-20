package com.fgallo94.invoices.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class InvoiceResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long internalCode;
    private Long number;
    private Character character;
    private String customerMail;
    private LocalDateTime dateTime;
    @OneToMany(targetEntity = Lines.class, mappedBy = "invoiceResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Lines> lines;
    private Double discount;
    private PayMethod payMethod;
    private Double recharge;
    @OneToOne(targetEntity = InvoiceStatus.class, mappedBy = "invoiceResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private InvoiceStatus invoiceStatus;

    public InvoiceResponse(Invoice invoice) {
        this.number = invoice.getNumber();
        this.character = invoice.getCharacter();
        this.customerMail = invoice.getCustomerMail();
        this.dateTime = invoice.getDateTime();
        this.lines = Arrays.asList(new Lines());
        this.discount = 0d;
        this.payMethod = PayMethod.CASH;
        this.recharge = 0d;
        this.invoiceStatus = new InvoiceStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceResponse that = (InvoiceResponse) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(character, that.character) &&
                Objects.equals(customerMail, that.customerMail) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(lines, that.lines) &&
                Objects.equals(discount, that.discount) &&
                payMethod == that.payMethod &&
                Objects.equals(recharge, that.recharge) &&
                Objects.equals(invoiceStatus, that.invoiceStatus);
    }

}
