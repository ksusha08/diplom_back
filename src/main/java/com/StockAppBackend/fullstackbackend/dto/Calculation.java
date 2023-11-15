package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Item;
import com.StockAppBackend.fullstackbackend.entity.Storehouse;

public class Calculation {

    private Storehouse storehouse;
    private Item item;
    private int minValue;
    private int reorderLevel;
    private int maxValue;

    public Calculation() {

    }


    public Calculation(Storehouse storehouse, Item item, int minValue, int reorderLevel, int maxValue) {
        this.storehouse = storehouse;
        this.item = item;
        this.minValue = minValue;
        this.reorderLevel = reorderLevel;
        this.maxValue = maxValue;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public Storehouse getStorehouse() {
        return storehouse;
    }

    public void setStorehouse(Storehouse storehouse) {
        this.storehouse = storehouse;
    }
}
