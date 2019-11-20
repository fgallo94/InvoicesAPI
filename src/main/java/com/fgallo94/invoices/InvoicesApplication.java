package com.fgallo94.invoices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = {InvoicesApplication.class, Jsr310JpaConverters.class}, basePackages = {"com.fgallo94.invoices.controller", "com.fgallo94.invoices.entity", "com.fgallo94.invoices.repository", "com.fgallo94.invoices.controller.service"})
public class InvoicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicesApplication.class, args);
    }

}
