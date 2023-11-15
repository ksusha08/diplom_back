package com.StockAppBackend.fullstackbackend.repo;

import com.StockAppBackend.fullstackbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
