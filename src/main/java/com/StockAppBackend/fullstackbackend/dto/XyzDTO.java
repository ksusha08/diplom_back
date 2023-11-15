package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Item;

public class XyzDTO {
    private Item item;
    private Double meanValue;
    private Double stdDeviation;


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getMeanValue() {
        return meanValue;
    }

    public void setMeanValue(double meanValue) {
        this.meanValue = meanValue;
    }

    public double getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(double stdDeviation) {
        this.stdDeviation = stdDeviation;
    }
}
