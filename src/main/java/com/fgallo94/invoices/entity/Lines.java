package com.fgallo94.invoices.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long internalCodes;
    @ManyToOne
    @JoinColumn(name = "invoiceResponse")
    @JsonIgnore
    private InvoiceResponse invoiceResponse;
    private String item;
    private String description;
    private Long quantity;
    private Double price;

    @Override
    public String toString() {
        return "{" +
                "\"item\": \"" + this.item + "\"," +
                "\"description\": \"" + this.description + "\"," +
                "\"quantity\": " + this.quantity + "," +
                "\"price\": " + this.price + "" +
                "}";
    }
}



