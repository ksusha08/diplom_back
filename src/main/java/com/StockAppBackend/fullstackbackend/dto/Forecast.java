package com.StockAppBackend.fullstackbackend.dto;

import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.Item;
import java.util.Date;


public class Forecast {

    private Item item;
    private Long amount;
    private String month;

    public Forecast(){}

    public Forecast(Item item, Long amount, String month) {
        this.item = item;
        this.amount = amount;
        this.month = month;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
