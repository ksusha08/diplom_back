package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Item;

import java.util.List;

public class CalculationRequest {
    private List<Long> items;

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
