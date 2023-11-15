package com.StockAppBackend.fullstackbackend.dto;


import com.StockAppBackend.fullstackbackend.entity.Document;
import com.StockAppBackend.fullstackbackend.entity.Item;

public class Report {
    private Long idinfo;
    private Document document;
    private Item item;
    private Long amount;
    private Double summ;

    public Report(){}

    public Report(Long idinfo, Document document, Item item, Long amount, Double summ) {
        this.idinfo = idinfo;
        this.document = document;
        this.item = item;
        this.amount = amount;
        this.summ = summ;
    }

    public Long getIdinfo() {
        return idinfo;
    }

    public void setIdinfo(Long idinfo) {
        this.idinfo = idinfo;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }
}
