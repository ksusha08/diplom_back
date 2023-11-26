package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;

@Entity
@Table(name="movent")
public class Movement {
    @Id
    @Column(name = "idmovement")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "id_storehouse_from")
    private Storehouse storehouseFrom;

    @ManyToOne
    @JoinColumn(name = "id_storehouse_to")
    private Storehouse storehouseTo;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;
}
