package com.StockAppBackend.fullstackbackend.repo;


import com.StockAppBackend.fullstackbackend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepo  extends JpaRepository<Expense, Long> {

    List<Expense> findByDocumentId(Long documentId);
}
