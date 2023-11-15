package com.StockAppBackend.fullstackbackend.repo;


import com.StockAppBackend.fullstackbackend.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  SupplierRepo extends JpaRepository<Supplier, Long> {
}
