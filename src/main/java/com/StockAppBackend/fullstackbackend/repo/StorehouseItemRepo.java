package com.StockAppBackend.fullstackbackend.repo;

import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;
import com.StockAppBackend.fullstackbackend.entity.StorehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorehouseItemRepo extends JpaRepository<StorehouseItem, Long> {

    StorehouseItem findByItemAndStorehouse(Item item, Storehouse storehouse);

    List<StorehouseItem> findByItemId(Long id);
}
