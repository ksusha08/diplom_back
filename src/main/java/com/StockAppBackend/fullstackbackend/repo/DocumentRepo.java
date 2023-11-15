package com.StockAppBackend.fullstackbackend.repo;

import com.StockAppBackend.fullstackbackend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DocumentRepo extends JpaRepository<Document, Long> {

    List<Document> findByNumberContainingIgnoreCase(String number);

    List<Document> findByDateGreaterThanEqualAndDateLessThanEqual(Date startDate, Date endDate);
}
