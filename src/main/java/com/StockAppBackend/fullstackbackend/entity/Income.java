package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;

@Entity
@Table(name="income")
public class Income {

    @Id
    @Column(name = "idincome")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_document")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @Column(name = "amount")
    private int amount;

    @Column(name = "coefficient_price")
    private Double coefficient_price;

    @Column(name = "summ")
    private Double summ;

    public Income(){}

    public Income(Long id, Document document, Double summ) {
        this.id = id;
        this.document = document;
        this.summ = summ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getCoefficient_price() {
        return coefficient_price;
    }

    public void setCoefficient_price(Double coefficient_price) {
        this.coefficient_price = coefficient_price;
    }
}
