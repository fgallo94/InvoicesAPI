package com.fgallo94.invoices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
/**
 *  POJO
 *
 */
public class InvoiceResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceStatus", referencedColumnName = "internalCode")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private InvoiceStatus invoiceStatus;

    public InvoiceResponse(Invoice invoice) {
        this.number = invoice.getNumber();
        this.character = invoice.getCharacter();
        this.customerMail = invoice.getCustomerMail();
        this.dateTime = invoice.getDateTime();
        this.lines = Collections.singletonList(new Lines());
        this.discount = 0d;
        this.payMethod = PayMethod.CASH;
        this.recharge = 0d;
        this.invoiceStatus = new InvoiceStatus();
    }

    @JsonIgnore
    public boolean isFinalized() {
        return Objects.nonNull(this.getInvoiceStatus()) && Objects.nonNull(this.getInvoiceStatus()
                .getFinalizedAt());
    }

}
