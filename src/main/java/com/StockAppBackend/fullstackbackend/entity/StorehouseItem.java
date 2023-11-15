package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;

@Entity
@Table(name="storehouses_items")
public class StorehouseItem {

    @Id
    @Column(name = "idstorehouseitem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_storehose")
    private Storehouse storehouse;

    @Column(name = "amount")
    private int amount;

    @Column(name = "min_amount")
    private int min_amount;

    @Column(name = "max_amount")
    private int max_amount;

    @Column(name = "reorder_level")
    private int reorder_level;

    public StorehouseItem(){}

    public StorehouseItem( Item item, Storehouse storehouse, int amount, int min_amount, int max_amount, int reorder_level) {
        this.item = item;
        this.storehouse = storehouse;
        this.amount = amount;
        this.min_amount = min_amount;
        this.max_amount = max_amount;
        this.reorder_level = reorder_level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Storehouse getStorehouse() {
        return storehouse;
    }

    public void setStorehouse(Storehouse storehouse) {
        this.storehouse = storehouse;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(int min_amount) {
        this.min_amount = min_amount;
    }

    public int getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(int max_amount) {
        this.max_amount = max_amount;
    }

    public int getReorder_level() {
        return reorder_level;
    }

    public void setReorder_level(int reorder_level) {
        this.reorder_level = reorder_level;
    }
}
