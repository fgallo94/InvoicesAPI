package com.fgallo94.invoices.repository;

import com.fgallo94.invoices.entity.InvoiceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceResponse, Long> {
}
