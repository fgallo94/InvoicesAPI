package com.fgallo94.invoices.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@Entity
class Lines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long internalCodes;
    @ManyToOne
    @JoinColumn(name = "invoiceResponse")
    private InvoiceResponse invoiceResponse;
    private String item;
    private String description;
    private Long quantity;
    private Double price;
}
