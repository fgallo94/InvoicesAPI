package com.fgallo94.invoices.repository;

import com.fgallo94.invoices.entity.Lines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinesRepository extends JpaRepository<Lines, Long> {
}
