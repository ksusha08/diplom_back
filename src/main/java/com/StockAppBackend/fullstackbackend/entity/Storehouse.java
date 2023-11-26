package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="storehouses")
public class Storehouse {
    @Id
    @Column(name = "idstorehouse")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "max_capacity")
    private int max_capacity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storehouse")
    private Set<StorehouseItem> storehouseItem = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storehouse")
    private Set<Document> document = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storehouseFrom")
    private Set<Movement> movementFrom = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storehouseTo")
    private Set<Movement> movementTo = new HashSet<>();

    public Storehouse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }
}
