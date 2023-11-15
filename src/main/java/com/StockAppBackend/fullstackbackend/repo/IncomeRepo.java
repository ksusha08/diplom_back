package com.StockAppBackend.fullstackbackend.repo;


import com.StockAppBackend.fullstackbackend.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepo extends JpaRepository<Income, Long> {

    List<Income> findByDocumentId(Long documentId);
}
