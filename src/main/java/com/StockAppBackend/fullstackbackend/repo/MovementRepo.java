package com.StockAppBackend.fullstackbackend.repo;

import com.StockAppBackend.fullstackbackend.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepo extends JpaRepository<Movement, Long> {
}
