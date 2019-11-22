package com.fgallo94.invoices.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
/**
 * POJO for the final step of the logic
 */
public class InvoiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long internalCode;
    private LocalDateTime finalizedAt;
    private LocalDateTime paidAt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "invoiceStatus")
    @JsonManagedReference
    private InvoiceResponse invoiceResponse;
}
