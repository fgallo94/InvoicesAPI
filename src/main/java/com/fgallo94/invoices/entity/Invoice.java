package com.fgallo94.invoices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * Initial object of the front-end
 */
public class Invoice {
    private Long number;
    private Character character;
    private String customerMail;
    private LocalDateTime dateTime;
}
