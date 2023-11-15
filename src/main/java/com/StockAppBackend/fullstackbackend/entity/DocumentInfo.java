package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;

@Entity
@Table(name="document_info")
public class DocumentInfo {

    @Id
    @Column(name = "idinfo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "summ")
    private Double summ;

    @Column(name = "coefficient_price")
    private Double coefficient_price;

    @ManyToOne
    @JoinColumn(name = "id_document")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    public DocumentInfo(){}

    public DocumentInfo(Long id, Integer amount, Double summ, Double coefficient_price, Document document, Item item) {
        this.id = id;
        this.amount = amount;
        this.summ = summ;
        this.coefficient_price = coefficient_price;
        this.document = document;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double price) {
        this.summ = price;
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

    public Double getCoefficient_price() {
        return coefficient_price;
    }

    public void setCoefficient_price(Double coefficient_price) {
        this.coefficient_price = coefficient_price;
    }
}
