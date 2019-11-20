package com.fgallo94.invoices.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Setter
@Entity
class Lines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long internalCodes;
    @ManyToOne
    @JoinColumn(name = "invoiceResponse")
    private InvoiceResponse invoiceResponse;
    private String item;
    private String description;
    private Long quantity;
    private Double price;
}
