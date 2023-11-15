package com.StockAppBackend.fullstackbackend.repo;


import com.StockAppBackend.fullstackbackend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo  extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByCategoryId(Long id);


}
