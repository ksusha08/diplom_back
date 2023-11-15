package com.StockAppBackend.fullstackbackend.repo;

import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorehouseRepo extends JpaRepository<Storehouse, Long> {
}
