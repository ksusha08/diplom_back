package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="suppliers")
public class Supplier {

    @Id
    @Column(name = "idsuppliers")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "coefficient")
    private Double coefficient;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier")
    private Set<Document> documents = new HashSet<>();

    public Supplier() {
    }

    public Supplier(Long id, String name, String email, String address, Double coefficient) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.coefficient = coefficient;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
