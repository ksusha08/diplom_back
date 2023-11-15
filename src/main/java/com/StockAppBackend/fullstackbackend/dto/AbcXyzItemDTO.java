package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Item;

public class AbcXyzItemDTO {
    private Item item;
    private String abcCategory;
    private String xyzCategory;
    private Double sales;

    public AbcXyzItemDTO(Item item, String abcCategory, String xyzCategory, Double sales) {
        this.item = item;
        this.abcCategory = abcCategory;
        this.xyzCategory = xyzCategory;
        this.sales = sales;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getAbcCategory() {
        return abcCategory;
    }

    public void setAbcCategory(String abcCategory) {
        this.abcCategory = abcCategory;
    }

    public String getXyzCategory() {
        return xyzCategory;
    }

    public void setXyzCategory(String xyzCategory) {
        this.xyzCategory = xyzCategory;
    }
}
